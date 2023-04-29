package org.jlogger.subcommands;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.jlogger.logger.Weaver;

import java.util.ArrayList;
import java.util.concurrent.Callable;


@Command(name = "profiler", description = "Gives CPU and Memory of methods", mixinStandardHelpOptions = true, version = "methodProfiler 1.0")
public class MethodProfiler implements Callable<Integer> {
  @CommandLine.Option(names = {"-m", "methods"}, description = "Methods to be profiled", required = true, arity = "1..*", defaultValue = "*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
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

    for (String method : methods) {
      methodsRegex.add(method);
    }

    weaver.weaverMethodProfiler(methodsRegex);
    if (aspect) {
      weaver.extractAspectjrtToJar();
    }
    weaver.saveWeavedJar(outputFile);

    return 0;
  }
}
