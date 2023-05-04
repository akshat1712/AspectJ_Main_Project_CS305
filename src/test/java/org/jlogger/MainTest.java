package org.jlogger;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void call() {
        int result = new CommandLine(new Main()).execute();
        assertEquals(0, result);
    }
}