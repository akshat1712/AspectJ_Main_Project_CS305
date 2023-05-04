package org.codetuner;

import org.codetuner.subcommands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * The main class for the Weaver command line tool. This class implements the Callable
 * interface to enable use with the picocli command line parsing library.
 */

@Command(name = "codetuner",
        mixinStandardHelpOptions = true,
        version = "codetuner 0.1",
        description = "A simple tool to weave jar files",
        subcommands = {ExecutionTime.class, MethodProfiler.class, Parallelize.class, Logging.class, Caching.class}
)

public class Main implements Callable<Integer> {

    /**
     * The main method that initializes the command line parser and executes the
     * program.
     *
     * @param args the command line arguments passed to the program
     */

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        if (exitCode == 0) {
            System.exit(0);
        }
        System.exit(1);
    }

    /**
     * The call method required by the Callable interface. This method is called when
     * the program is executed and delegates the work to other methods.
     *
     * @return an integer exit code indicating whether the program executed successfully
     * @throws Exception if an error occurs while executing the program
     */

    @Override
    public Integer call() throws Exception {
        System.out.println("Use --help for more information");
        return 0;
    }
}