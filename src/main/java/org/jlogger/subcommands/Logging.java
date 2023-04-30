package org.jlogger.subcommands;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.jlogger.logger.Weaver;

import java.util.ArrayList;
import java.util.concurrent.Callable;


@Command(name = "logging", description = "extensively logs program execution", mixinStandardHelpOptions = true, version = "Logging 1.0")
public class Logging implements Callable<Integer> {
    @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be Logged", required = true, arity = "1..*", defaultValue = "*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String[] methods;
    @CommandLine.Option(names = {"-v", "variables"}, description = "Variables to be Logged", required = true, arity = "1..*", defaultValue = "", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String[] variables;
    @CommandLine.Option(names = {"-l", "--logfile"}, description = "Log file name", defaultValue = "")
    private String logFile;
    @CommandLine.Option(names = {"-a", "--aspect"}, description = "Add aspectjrt to path")
    private boolean aspect;
    @CommandLine.Option(names = {"-i", "--input"}, description = "Path to the jar file to be weaved", defaultValue = "")
    private String inputFile;
    @CommandLine.Option(names = {"-o", "--output"}, description = "Path to the output file", defaultValue = "weaved.jar")
    private String outputFile;
    @Override
    public Integer call() throws Exception {
        Weaver weaver = new Weaver(logFile, inputFile);

        ArrayList<String> methodsRegex = new ArrayList<>();
        ArrayList<String> variablesRegex = new ArrayList<>();
        for (String method : methods) {
            methodsRegex.add(method);
        }

        for (String variable : variables) {
            variablesRegex.add(variable);
        }

        weaver.weaveLogging(methodsRegex, variablesRegex);

        if (aspect) {
            weaver.extractAspectjrtToJar();
        }
        weaver.saveWeavedJar(outputFile);
        return 0;
    }
}
