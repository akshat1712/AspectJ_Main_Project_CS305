package org.jlogger.subcommands;

import org.jlogger.logger.Weaver;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;


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
