package org.jlogger.logger;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WeaverTest {

    static Weaver weaver;

    @BeforeAll
    static void setUp() {
        // Initialize weaver with logfilepath as System.out and jarpath as test.jar
        weaver = new Weaver(""
                , Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "test.jar").toString());
    }

    @Test
    @Order(1)
    void weaveMethodExecutionTime() {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("*");
        boolean result = weaver.weaveMethodExecutionTime(methods);
        assertTrue(result);
    }

    @Test
    @Order(2)
    void weaverMethodProfiler() {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("method1");
        boolean result = weaver.weaverMethodProfiler(methods);
        assertTrue(result);
    }

    @Test
    @Order(3)
    void weaveLogging() {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("method2");
        ArrayList<String> fieldregex = new ArrayList<>();
        fieldregex.add("MyService.*");
        boolean result = weaver.weaveLogging(methods, fieldregex);
        assertTrue(result);
    }

    @Test
    @Order(4)
    void weaverParallelize() {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("method1");
        methods.add("method2");
        boolean result = weaver.weaverParallelize(methods);
        assertTrue(result);
    }

    @Test
    void weaverCaching() {
        ArrayList<String> methods = new ArrayList<>();
        methods.add("method1");
        methods.add("method2");
        boolean result = weaver.weaverCaching(methods);
        assertTrue(result);
    }

    @Test
    @Order(6)
    void saveWeavedJar() {
        weaver.saveWeavedJar("weaved.jar");
        // Check if the weaved jar exists
        assertTrue(Paths.get(System.getProperty("user.dir"), "weaved.jar").toFile().exists());

    }

    @Test
    @Order(5)
    void extractAspectjrtToJar() {
        assertTrue(weaver.extractAspectjrtToJar());
    }

    @Test
    @Order(7)
    void deleteWeavedJar() throws IOException {
        assertTrue(Files.deleteIfExists(Paths.get(System.getProperty("user.dir"), "weaved.jar")));
        assertFalse(Paths.get(System.getProperty("user.dir"), "weaved.jar").toFile().exists());
    }
}