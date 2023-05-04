package org.codetuner.subcommands;

import org.codetuner.weaver.Weaver;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * The `logging` sub command of `codetuner` is a command-line utility that logs program execution extensively. This sub command is designed to help developers debug their Java code.
 *
 * <p>Usage:
 * <pre>{@code codetuner logging --methods <methodNames> --variables <variableNames> [--logfile <logFile>] [--aspect] [--input <inputFile>] [--output <outputFile>]}</pre>
 *
 * <p>Options:
 * <ul>
 *     <li>{@code -m, --methods}      Methods to be logged. Can be a regular expression or the wildcard '*'. This option is required.</li>
 *     <li>{@code -v, --variables}    Variables to be logged. Can be a regular expression or the wildcard '*'. This option is required.</li>
 *     <li>{@code -l, --logfile}      Log file name. Default is an empty string, which means logging is disabled.</li>
 *     <li>{@code -a, --aspect}       Add aspectjrt library to path.</li>
 *     <li>{@code -i, --input}        Path to the jar file to be weaved. Default is an empty string, which means no input file is provided.</li>
 *     <li>{@code -o, --output}       Path to the output file. Default is 'weaved.jar'.</li>
 * </ul>
 *
 * <p>Example: To log execution of all methods in a class named 'MyClass' and all variables in the class run:
 * <pre>{@code codetuner logging --methods MyClass.* --variables MyClass.* --input MyClass.jar --aspect --output weaved.jar}</pre>
 *
 * <p>Version:  1.0
 */
@Command(name = "logging", description = "extensively logs program execution", mixinStandardHelpOptions = true, version = "Logging 1.0")
public class Logging implements Callable<Integer> {
    @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be Logged", required = true, arity = "1..*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String[] methods;
    @CommandLine.Option(names = {"-v", "variables"}, description = "Variables to be Logged", required = true, arity = "1..*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
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
        Collections.addAll(methodsRegex, methods);

        Collections.addAll(variablesRegex, variables);

        weaver.weaveLogging(methodsRegex, variablesRegex);

        if (aspect) {
            weaver.extractAspectjrtToJar();
        }
        weaver.saveWeavedJar(outputFile);
        return 0;
    }
}
