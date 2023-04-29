package org.jlogger;

import org.jlogger.subcommands.MethodProfiler;
import org.jlogger.subcommands.Parallelize;
import org.jlogger.subcommands.Logging;
import org.jlogger.subcommands.ExecutionTime;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "jlogger",
mixinStandardHelpOptions = true,
        version = "jlogger 0.1",
        description = "A simple tool to weave jar files",
        subcommands = {ExecutionTime.class, MethodProfiler.class, Parallelize.class, Logging.class}
)

public class Main implements Callable<Integer> {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        if (exitCode == 0) {
            System.exit(0);
        }
        System.exit(1);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Use --help for more information");
        return 0;
    }
}