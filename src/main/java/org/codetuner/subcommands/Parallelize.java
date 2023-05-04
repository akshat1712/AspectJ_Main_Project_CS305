package org.codetuner.subcommands;

import org.codetuner.weaver.Weaver;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * The `parallelize` sub command of `codetuner` is a command-line utility that parallelize methods. This program is designed to help developers optimize the performance of their Java code.
 *
 * <p>Usage:
 * <pre>{@code codetuner parallelize --methods <methodNames> [--logfile <logFile>] [--aspect] [--input <inputFile>] [--output <outputFile>]}</pre>
 *
 * <p>Options:
 * <ul>
 *     <li>{@code -m, --methods}      Methods to be executed in Parallel. Can be a regular expression or the wildcard '*'. This option is required.</li>
 *     <li>{@code -l, --logfile}      Log file name. Default is an empty string, which means logging is disabled.</li>
 *     <li>{@code -a, --aspect}       Add aspectjrt library to path.</li>
 *     <li>{@code -i, --input}        Path to the jar file to be weaved. Default is an empty string, which means no input file is provided.</li>
 *     <li>{@code -o, --output}       Path to the output file. Default is 'weaved.jar'.</li>
 * </ul>
 *
 * <p>Example: To parallelize all methods in a class named 'MyClass' and 'run' method in MyClass2 run:
 * <pre>{@code codetuner parallelize --methods MyClass.* MyClass2.run --input MyClass.jar --aspect --output weaved.jar}</pre>
 *
 * <p>Version:  1.0
 */
@Command(name = "parallelize", description = "Parallelize the methods which can be concurrent", mixinStandardHelpOptions = true, version = "parallel 1.0")
public class Parallelize implements Callable<Integer> {

    @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be executed in Parallel", required = true, arity = "1..*", defaultValue = "*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
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

        weaver.weaverParallelize(methodsRegex);

        if (aspect) {
            weaver.extractAspectjrtToJar();
        }
        weaver.saveWeavedJar(outputFile);
        return 0;
    }
}
