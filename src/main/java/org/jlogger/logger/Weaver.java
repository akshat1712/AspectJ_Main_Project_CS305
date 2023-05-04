package org.jlogger.logger;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;


public class Weaver {
    private final String workDir = System.getProperty("user.home") + System.getProperty("file.separator") + ".jloggertemp";
    private final String aroundMethodPlaceHolder = "execution($2 $1(..))";
    private final String aroundFieldSetPlaceHolder = "set($2 $1)";
    private final String aroundFieldGetPlaceHolder = "get($2 $1)";

    private final String[] returnTypesArray = {"Integer", "int", "Long", "long", "Float", "float", "Double", "double", "String", "Boolean", "boolean", "Byte", "byte", "Short", "short", "Character", "char"};
    private final MessageHandler messageHandler;
    public String logFilePath;
    public String jarInputPath;
    private String lastWeavedJarPath = "";

    public Weaver(String logFilePath, String jarInputPath) throws IOException {
        // If the temp directory is not present in workDir, create it

        if (new java.io.File(workDir).exists()) {
                Files.walk(Paths.get(workDir))
                        .map(Path::toFile)
                        .forEach(File::delete);

        }
        new java.io.File(workDir).mkdir();

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
            // Get the file contents of ExecutionTimeAspect.java from the resources folder
            InputStream executionTimeAspect = Weaver.class.getClassLoader().getResourceAsStream("ExecutionTimeAspect.java");

            String executionTimeAspectContents = new String(executionTimeAspect.readAllBytes());
            // Replace the placeholder in the aspect with the log file path

            executionTimeAspectContents = executionTimeAspectContents.replace("${logFileName}", logFilePath);
            executionTimeAspectContents = executionTimeAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, "*"));
            System.out.println(prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, "*"));
            for (String methodRegex : methodsRegex) {
                System.out.println("REGEX: " + methodRegex);
            }

            // Write the modified aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "ExecutionTimeAspect.java");
            writer.write(executionTimeAspectContents);
            writer.close();

            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            //Set output file location without full path
            outputFileName = outputFileName.substring(outputFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
            System.out.println("Output file name: " + outputFileName);
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "ExecutionTimeAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            jarInputPath = this.lastWeavedJarPath;
            System.out.println("LAST WEAVED JAR PATH: " + this.lastWeavedJarPath);

            return true;
        } catch (Exception e) {
            System.err.println("Some unexpected error occurred while weaving the jar. Check the method names and try again");
            return false;
        }
    }

    public boolean weaverMethodProfiler(ArrayList<String> methodsRegex) {
        try {
            // Get the file contents of ExecutionTimeAspect.java from the resources folder
            InputStream methodProfilerAspect = Weaver.class.getClassLoader().getResourceAsStream("MethodProfilerAspect.java");

            String methodProfilerAspectContents = new String(methodProfilerAspect.readAllBytes());
            // Replace the placeholder in the aspect with the log file path

            methodProfilerAspectContents = methodProfilerAspectContents.replace("${logFileName}", logFilePath);
            methodProfilerAspectContents = methodProfilerAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, "*"));

            // Write the modified aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "MethodProfilerAspect.java");
            writer.write(methodProfilerAspectContents);
            writer.close();
            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            //Set output file location without full path
            outputFileName = outputFileName.substring(outputFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "MethodProfilerAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            jarInputPath = this.lastWeavedJarPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean weaveLogging(ArrayList<String> methodsRegex, ArrayList<String> fieldsRegex) {
        try {
            // Get the file contents of LoggingAspect.java from the resources folder
            InputStream LoggingAspect = Weaver.class.getClassLoader().getResourceAsStream("LoggingAspect.java");
            String LoggingAspectContents = new String(LoggingAspect.readAllBytes());
            // Replace the placeholder in the aspect with the log file path

            LoggingAspectContents = LoggingAspectContents.replace("${logFileName}", logFilePath);
            LoggingAspectContents = LoggingAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, "*"));
            LoggingAspectContents = LoggingAspectContents.replace("${FieldSetNames}", prepareFinalRegex(fieldsRegex, aroundFieldSetPlaceHolder, "*"));
            LoggingAspectContents = LoggingAspectContents.replace("${FieldGetNames}", prepareFinalRegex(fieldsRegex, aroundFieldGetPlaceHolder, "*"));

            // Write the modified aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "LoggingAspect.java");
            writer.write(LoggingAspectContents);
            writer.close();
            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            //Set output file location without full path
            outputFileName = outputFileName.substring(outputFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "LoggingAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            jarInputPath = this.lastWeavedJarPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean weaverParallelize(ArrayList<String> methodsRegex) {
        try {
            // Get the file contents of ExecutionTimeAspect.java from the resources folder
            InputStream parallelizeAspect = Weaver.class.getClassLoader().getResourceAsStream("ParallelizeAspect.java");
            String parallelizeAspectContents = new String(parallelizeAspect.readAllBytes());

            parallelizeAspectContents = parallelizeAspectContents.replace("${logFileName}", logFilePath);
            parallelizeAspectContents = parallelizeAspectContents.replace("${MethodNames}", prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, "*"));

            // Write the  aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "ParallelizeAspect.java");
            writer.write(parallelizeAspectContents);
            writer.close();

            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            //Set output file location without full path
            outputFileName = outputFileName.substring(outputFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "ParallelizeAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            jarInputPath = this.lastWeavedJarPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean weaverCaching(ArrayList<String> methodsRegex) {
        try {
            // Get the file contents of ExecutionTimeAspect.java from the resources folder
            InputStream cachingAspect = Weaver.class.getClassLoader().getResourceAsStream("CachingAspect.java");
            String cachingAspectContents = new String(cachingAspect.readAllBytes());

            cachingAspectContents = cachingAspectContents.replace("${logFileName}", logFilePath);

            // loop over returnTypesArray
            for (String returnType : returnTypesArray) {
                String methodName = "${MethodNames" + returnType + "}";
                cachingAspectContents = cachingAspectContents.replace(methodName, prepareFinalRegex(methodsRegex, aroundMethodPlaceHolder, returnType));
            }

            // Write the  aspect to the temp directory
            FileWriter writer = new FileWriter(workDir + System.getProperty("file.separator") + "CachingAspect.java");
            writer.write(cachingAspectContents);
            writer.close();

            String outputFileName = this.jarInputPath.replace(".jar", "_weaved.jar");
            //Set output file location without full path
            outputFileName = outputFileName.substring(outputFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
            weaveJarFile(jarInputPath, workDir + System.getProperty("file.separator") + "CachingAspect.java", workDir + System.getProperty("file.separator") + outputFileName);
            this.lastWeavedJarPath = workDir + System.getProperty("file.separator") + outputFileName;
            jarInputPath = this.lastWeavedJarPath;
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

            aspectjrtJar.close();
            this.lastWeavedJarPath = outputJarPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String prepareFinalRegex(ArrayList<String> methodsRegex, String PlaceHolder, String returnType) {

        System.out.println(PlaceHolder);

        String finalRegex = "";
        for (String regex : methodsRegex) {
            finalRegex += PlaceHolder.replace("$1", regex).replace("$2", returnType) + " || ";
        }
        System.out.println(finalRegex.substring(0, finalRegex.length() - 4));
        System.out.flush();

        return finalRegex.substring(0, finalRegex.length() - 4);    // Remove the last " || "
    }

    private boolean weaveJarFile(String jarFilePath, String aspectFilePath, String outjarPath) throws Exception {
        System.out.println("JARFILEPATH: " + jarFilePath);
        System.out.println("ASPECTFILEPATH: " + aspectFilePath);
        System.out.println("OUTJARPATH: " + outjarPath);

        String[] args = {"-source", "1.8", "-target", "1.8", "-inpath", jarFilePath, aspectFilePath, "-outjar", outjarPath};
        Main main = new Main();
        main.run(args, this.messageHandler);
        // Print the messages
        for (IMessage message : this.messageHandler.getMessages(null, true)) {
            System.out.println(message.toString());
        }
        return true;
    }
}