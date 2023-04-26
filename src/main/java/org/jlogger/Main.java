package org.jlogger;

import org.jlogger.logger.Weaver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Weaver weaver = new Weaver("parallelize.txt", "input.jar");
        ArrayList<String> methodsRegex = new ArrayList<>();
        ArrayList<String> fieldsSetRegex = new ArrayList<>();
        ArrayList<String> fieldsGetRegex = new ArrayList<>();

        methodsRegex.add("*(..)");
//        weaver.weaveExecutionTime(methodsRegex);
//        weaver.weaverMethodProfiler(methodsRegex);
        fieldsGetRegex.add("MyService.*");
        fieldsSetRegex.add("MyService.*");

        weaver.weaverParallelize(methodsRegex);
//          weaver.weaverMethodProfiler(methodsRegex);
//        weaver.weaveMethodExecutionTime(methodsRegex);
//        boolean t =weaver.weaveLogging(methodsRegex,fieldsSetRegex,fieldsGetRegex);
//        System.out.println(t);



        weaver.extractAspectjrtToJar();
        weaver.saveWeavedJar("parallelize.jar");
    }
}