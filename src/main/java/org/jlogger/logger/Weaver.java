package org.jlogger.logger;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Weaver {
    private final String workDir = System.getProperty("user.home") + System.getProperty("file.separator") + ".jloggertemp";
    private final String aroundMethodPlaceHolder = "execution(* $1)";
    private final String aroundFieldSetPlaceHolder = "set(* $1)";
    private final String aroundFieldGetPlaceHolder = "get(* $1)";
    private final MessageHandler messageHandler;
    public String logFilePath;
    public String jarInputPath;
    private String lastWeavedJarPath = "";

    public Weaver(String logFilePath, String jarInputPath) {
        // If the temp directory is not present in workDir, create it
        if (!new java.io.File(workDir).exists()) {
            new java.io.File(workDir).mkdir();
        }
        this.logFilePath = logFilePath;
        this.jarInputPath = jarInputPath;
        // Create Imessage holder
        this.messageHandler = new MessageHandler(true);

    }

    private static void addFilesToJar(String sourceFolder, JarOutputStream jos, String parentFolder) throws IOException {
        File folder = new File(sourceFolder);

        for (File file : folder.listFiles()) {
            String entryName = parentFolder + file.getName();
            if (file.isDirectory()) {
                entryName += "/";
                jos.putNextEntry(new JarEntry(entryName));
                jos.closeEntry();
                addFilesToJar(file.getAbsolutePath(), jos, entryName);
            } else {
                jos.putNextEntry(new JarEntry(entryName));
                byte[] buffer = new byte[1024];
                int bytesRead;
                FileInputStream fileInputStream = new FileInputStream(file);
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    jos.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
                jos.closeEntry();
            }
        }
    }

    public boolean weaveMethodExecutionTime(ArrayList<String> methodsRegex) {
        try {
            // Get the file contents of ExecutionTimeAspect.aj from the resources folder
            InputStream executionTimeAspect = Weaver.class.getClassLoader().getResourceAsStream("ExecutionTimeAspect.aj");

            String executionTimeAspectContents = new String(executionTimeAspect.readAllBytes());
            // Replace the placeholder in the aspect with the log file path

            executionTimeAspectContents = executionTimeAspectContents.replace("${logFileName}", logFilePath);
            executionTimeAspectContents = executionTimeAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex,aroundMethodPlaceHolder));

            // Write the modified aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "ExecutionTimeAspect.aj");
            writer.write(executionTimeAspectContents);
            writer.close();

            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "ExecutionTimeAspect.aj", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean weaveLogging(ArrayList <String> methodsRegex, ArrayList <String> fieldsSetRegex, ArrayList <String> fieldsGetRegex) {
        try {
            // Get the file contents of LoggingAspect.java from the resources folder
            InputStream LoggingAspect = Weaver.class.getClassLoader().getResourceAsStream("LoggingAspect.java");
            String LoggingAspectContents = new String(LoggingAspect.readAllBytes());
            // Replace the placeholder in the aspect with the log file path

            LoggingAspectContents = LoggingAspectContents.replace("${logFileName}", logFilePath);
            LoggingAspectContents = LoggingAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex,aroundMethodPlaceHolder));
            LoggingAspectContents = LoggingAspectContents.replace("${FieldSetNames}", prepareFinalRegex(fieldsSetRegex,aroundFieldSetPlaceHolder));
            LoggingAspectContents = LoggingAspectContents.replace("${FieldGetNames}", prepareFinalRegex(fieldsGetRegex,aroundFieldGetPlaceHolder));

            // Write the modified aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "LoggingAspect.java");
            writer.write(LoggingAspectContents);
            writer.close();
            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "LoggingAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveWeavedJar(String path) {
        try {
            java.nio.file.Files.copy(new java.io.File(this.lastWeavedJarPath).toPath(), new java.io.File(path).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean extractAspectjrtToJar() {
        try {
            ClassLoader classLoader = Weaver.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("aspectjrt.jar");
            //Copy this file to working directory first
            java.nio.file.Files.copy(inputStream, new java.io.File(workDir + System.getProperty("file.separator") + "aspectjrt.jar").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            String inputJarPath = this.lastWeavedJarPath;
            String outputJarPath = this.lastWeavedJarPath.replace(".jar", "_aspectjrt.jar");
            // Copy the contents of the input jar to the output jar
            JarFile inputJar = new JarFile(inputJarPath);
            JarOutputStream outputJar = new JarOutputStream(new FileOutputStream(outputJarPath));
            String JarExtractPath = workDir + System.getProperty("file.separator") + "JarExtract";
            // If the path exists, delete it
            if (new java.io.File(JarExtractPath).exists()) {
                new java.io.File(JarExtractPath).delete();
            }
            // Create the path
            new java.io.File(JarExtractPath).mkdir();
            // Extract the aspectjrt.jar to the JarExtract directory
            JarFile aspectjrtJar = new JarFile(workDir + System.getProperty("file.separator") + "aspectjrt.jar");
            Enumeration<JarEntry> entries = aspectjrtJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().contains("META-INF") || entry.getName().contains("MANIFEST.MF")) {
                    continue;
                }
                if (entry.isDirectory()) {
                    new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).mkdir();
                } else {
                    Files.createDirectories(new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).toPath().getParent());
                    java.nio.file.Files.copy(aspectjrtJar.getInputStream(entry), new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Extract the input jar to the JarExtract directory
            entries = inputJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    // If the directory does not exist, create it
                    if (!new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).exists()) {
                        new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).mkdir();
                    }
                } else {
                    Files.createDirectories(new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).toPath().getParent());
                    java.nio.file.Files.copy(inputJar.getInputStream(entry), new java.io.File(JarExtractPath + System.getProperty("file.separator") + entry.getName()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }
            // Put everything inside the JarExtract directory to the output jar

            addFilesToJar(JarExtractPath, outputJar, "");

            inputJar.close();
            outputJar.close();
            this.lastWeavedJarPath = outputJarPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String prepareFinalRegex(ArrayList<String> methodsRegex,String PlaceHolder) {

        System.out.println(PlaceHolder);

        String finalRegex = "";
        for (String regex : methodsRegex) {
            finalRegex += PlaceHolder.replace("$1", regex) + " || ";
        }
        System.out.println(finalRegex.substring(0, finalRegex.length() - 4));
        System.out.flush();

        return finalRegex.substring(0, finalRegex.length() - 4);    // Remove the last " || "
    }

    private boolean weaveJarFile(String jarFilePath, String aspectFilePath, String outjarPath) {
        try {
            String[] args = {"-source", "1.8", "-target", "1.8", "-inpath", jarFilePath, aspectFilePath, "-outjar", outjarPath};
            Main main = new Main();
            main.run(args, this.messageHandler);
            // Print the messages
            for (IMessage message : this.messageHandler.getMessages(null, true)) {
                System.out.println(message.toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}