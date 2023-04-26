package org.jlogger;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.jlogger.subcommands.ExecutionTime;
import java.util.concurrent.Callable;

@Command(name = "jlogger",
mixinStandardHelpOptions = true,
        version = "jlogger 0.1",
        description = "A simple tool to weave jar files",
        subcommands = {ExecutionTime.class}
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