package org.codetuner.subcommands;

import org.codetuner.weaver.Weaver;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * The `caching` sub command of `codetuner` is a command-line utility that caches methods. This program is designed to help developers optimize the performance of their Java code.
 *
 * <p>Usage:
 * <pre>{@code codetuner caching --methods <methodNames> [--logfile <logFile>] [--aspect] [--input <inputFile>] [--output <outputFile>]}</pre>
 *
 * <p>Options:
 * <ul>
 *     <li>{@code -m, --methods}      Methods to be cached. Can be a regular expression or the wildcard '*'. This option is required.</li>
 *     <li>{@code -l, --logfile}      The name of the log file. Default is an empty string, which means logging is disabled.</li>
 *     <li>{@code -a, --aspect}       Add the aspectjrt library to the classpath. This option is optional.</li>
 *     <li>{@code -i, --input}        Path to the jar file to be weaved. Default is an empty string, which means no input file is provided.</li>
 *     <li>{@code -o, --output}       Path to the output file. Default is 'weaved.jar'.</li>
 * </ul>
 *
 * <p>Example: To cache all methods in a class named 'MyClass' and 'run' method in MyClass2 run:
 * <pre>{@code codetuner caching --methods MyClass.* MyClass2.run --input MyClass.jar --aspect --output weaved.jar}</pre>
 *
 * <p>Version:  1.0
 */

@Command(name = "caching", description = "Cache methods with primitive arguments", mixinStandardHelpOptions = true, version = "cache 1.0")
public class Caching implements Callable<Integer> {

    @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be cached", required = true, arity = "1..*", defaultValue = "*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String[] methods;

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
        Collections.addAll(methodsRegex, methods);
        weaver.weaverCaching(methodsRegex);

        if (aspect) {
            weaver.extractAspectjrtToJar();
        }
        weaver.saveWeavedJar(outputFile);
        return 0;
    }
}
