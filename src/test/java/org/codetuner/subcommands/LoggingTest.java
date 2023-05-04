package org.codetuner.subcommands;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingTest {

    @Test
    void call() {
        int result = new CommandLine(new Logging()).execute("-m", "method2", "-i", "src\\test\\resources\\test.jar", "-o", "weaved.jar", "-v", "MyService.*");
        assertEquals(0, result);
        //Check if weaved.jar is created
        assertTrue(new java.io.File("weaved.jar").exists());
        //Delete weaved.jar
        assertTrue(new java.io.File("weaved.jar").delete());
        //With aspectjrt
        result = new CommandLine(new Logging()).execute("-m", "method2", "-i", "src\\test\\resources\\test.jar", "-o", "weaved.jar", "-a", "-v", "MyService.*");
        assertEquals(0, result);
        //Check if weaved.jar is created
        assertTrue(new java.io.File("weaved.jar").exists());
        //Delete weaved.jar
        assertTrue(new java.io.File("weaved.jar").delete());
    }
}