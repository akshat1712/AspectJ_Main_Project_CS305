package org.jlogger;

import org.jlogger.logger.Weaver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Weaver weaver = new Weaver("", "input.jar");
        ArrayList<String> methodsRegex = new ArrayList<>();
        methodsRegex.add("*(..)");
        weaver.weaveExecutionTime(methodsRegex);
        weaver.extractAspectjrtToJar();
        weaver.saveWeavedJar("weaved.jar");
    }
}