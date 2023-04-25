package org.jlogger;

import org.jlogger.logger.Weaver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Weaver weaver = new Weaver("", "input.jar");
        ArrayList<String> methodsRegex = new ArrayList<>();
        methodsRegex.add("*(..)");

//        ArrayList<String> fieldsSetRegex = new ArrayList<>();
//        ArrayList<String> fieldsGetRegex = new ArrayList<>();

//        fieldsSetRegex.add("MyService.*");
//        weaver.weaveMethodExecutionTime(methodsRegex);
        weaver.weaveMethodLogging(methodsRegex);
//        weaver.weaveFieldSetLogging(fieldsSetRegex);

        weaver.extractAspectjrtToJar();
        weaver.saveWeavedJar("weaved.jar");
    }
}