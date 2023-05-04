package org.jlogger.subcommands;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

class CachingTest {

    @Test
    void call() {
        //Without aspectjrt
        int result = new CommandLine(new Caching()).execute("-m", "method1", "-i", "src\\test\\resources\\test.jar", "-o", "weaved.jar");
        assertEquals(0, result);
        //Check if weaved.jar is created
        assertTrue(new java.io.File("weaved.jar").exists());
        //Delete weaved.jar
        assertTrue(new java.io.File("weaved.jar").delete());
        //With aspectjrt
        result = new CommandLine(new Caching()).execute("-m", "method1", "-i", "src\\test\\resources\\test.jar", "-o", "weaved.jar", "-a");
        assertEquals(0, result);
        //Check if weaved.jar is created
        assertTrue(new java.io.File("weaved.jar").exists());
        //Delete weaved.jar
        assertTrue(new java.io.File("weaved.jar").delete());
    }
}