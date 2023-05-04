package org.codetuner.subcommands;

import org.codetuner.weaver.Weaver;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * The `time` sub command of `codetuner` is a command-line utility that logs the execution time of methods. This program is designed to help developers record the time taken by methods of their Java code.
 *
 * <p>Usage:
 * <pre>{@code codetuner time [--methods <methodNames>] [--logfile <logFile>] [--aspect] [--input <inputFile>] [--output <outputFile>]}</pre>
 *
 * <p>Options:
 * <ul>
 *     <li>{@code -m, --methods}      Methods to be measured. Can be a regular expression or the wildcard '*'. Default is '*', which means all methods in the provided input file are measured.</li>
 *     <li>{@code -l, --logfile}      Log file name. Default is an empty string, which means logging is disabled.</li>
 *     <li>{@code -a, --aspect}       Add aspectjrt library to path.</li>
 *     <li>{@code -i, --input}        Path to the jar file to be weaved. Default is an empty string, which means no input file is provided.</li>
 *     <li>{@code -o, --output}       Path to the output file. Default is 'weaved.jar'.</li>
 * </ul>
 *
 * <p>Example: To measure the execution time of all methods in a class named 'MyClass' and 'run' method in MyClass2 run:
 * <pre>{@code codetuner time --methods MyClass.* MyClass2.run --input MyClass.jar --aspect --output weaved.jar}</pre>
 *
 * <p>Version:  1.0
 */

@Command(name = "time", description = "Log execution time of methods", mixinStandardHelpOptions = true, version = "executionTime 1.0")
public class ExecutionTime implements Callable<Integer> {
    @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be measured", arity = "1..*", defaultValue = "*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String[] methods;
    @CommandLine.Option(names = {"-l", "--logfile"}, description = "Log file name", defaultValue = "")
    private String logFile;
    @CommandLine.Option(names = {"-a", "--aspect"}, description = "Add aspectjrt to path")
    private boolean aspect;
    @CommandLine.Option(names = {"-i", "--injar"}, description = "Path to the jar file to be weaved", required = true)
    private String inputFile;
    @CommandLine.Option(names = {"-o", "--outjar"}, description = "Path to the output file", defaultValue = "weaved.jar")
    private String outputFile;

    @Override
    public Integer call() throws Exception {
        Weaver weaver = new Weaver(logFile, inputFile);
        ArrayList<String> methodsRegex = new ArrayList<>();

        Collections.addAll(methodsRegex, methods);
        weaver.weaveMethodExecutionTime(methodsRegex);
        if (aspect) {
            weaver.extractAspectjrtToJar();
        }
        weaver.saveWeavedJar(outputFile);
        return 0;
    }
}
