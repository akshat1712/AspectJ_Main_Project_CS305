package org.jlogger;

import org.jlogger.logger.Weaver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Weaver weaver = new Weaver("", "input.jar");
        ArrayList<String> methodsRegex = new ArrayList<>();
        ArrayList<String> fieldsSetRegex = new ArrayList<>();
        ArrayList<String> fieldsGetRegex = new ArrayList<>();

        methodsRegex.add("*(..)");
        fieldsGetRegex.add("*.*");
        fieldsSetRegex.add("*.*");


//        weaver.weaveMethodExecutionTime(methodsRegex);
////        weaver.jarInputPath= "input_weaved.jar";
        weaver.weaveLogging(methodsRegex,fieldsSetRegex,fieldsGetRegex);



        weaver.extractAspectjrtToJar();
        weaver.saveWeavedJar("weaved.jar");
    }
}