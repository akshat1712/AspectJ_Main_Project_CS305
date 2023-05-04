import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.FileWriter;
import java.io.PrintWriter;

@Aspect
public class LoggingAspect {

    static int depth = 0;

    PrintWriter writer;

    public LoggingAspect() throws Exception {

        String logFileName = "${logFileName}";
        if (logFileName != null && !logFileName.isEmpty()) {
            writer = new PrintWriter(new FileWriter(logFileName, true));
        } else {
            writer = new PrintWriter(System.out);
        }
    }


    @Pointcut("!within(ParallelizeAspect) || !within(CachingAspect) || !within(ExecutionTimeAspect) || !within(LoggingAspect) || !within(MethodProfilerAspect)")
    public void excludeAspectClasses() {
    }

    @Before("(${MethodNames}) && excludeAspectClasses()")
    //This method is used to log the beginning of the methods
    public void LogMethodEntry(JoinPoint joinPoint) {

        String message;
        depth += 1;
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            message = "Entering " + joinPoint.getSignature().getName() + " With No arguments";
            printLogMessage(message);
            return;
        }

        message = "Entering " + joinPoint.getSignature().getName() + " With Following arguments";
        printLogMessage(message);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {

            String type = getTypeName(args[i]);

            if (type == null) {
                message = "Arguement " + parameterNames[i] + ": " + args[i];
                printLogMessage(message);
            } else {
                message = "Arguement " + parameterNames[i] + " of TYPE " + type;
                printLogMessage(message);
            }

        }
    }

    @AfterReturning(pointcut = "(${MethodNames}) && excludeAspectClasses()", returning = "result")
    //This method is used to log the end of the methods
    public void LogMethodReturn(JoinPoint joinPoint, Object result) throws Exception {

        String message;
        message = "Exiting " + joinPoint.getSignature().getName();
        printLogMessage(message);

        String type = getTypeName(result);

        if (type == null)
            message = "Returned value of " + joinPoint.getSignature().getName() + ": " + result;
        else
            message = "Returned value of " + joinPoint.getSignature().getName() + " of TYPE " + type;

        printLogMessage(message);

        depth -= 1;
    }

    @AfterThrowing(pointcut = "(${MethodNames}) && excludeAspectClasses()", throwing = "exception")
    //This method is used to log the exceptions thrown by the methods
    public void logMethodException(JoinPoint joinPoint, Exception exception) throws Exception {

        String message = "Exception caught in " + joinPoint.getSignature().getName() + " of CLASS " + joinPoint.getTarget().getClass().getName();

        printLogMessage(message);

        depth -= 1;
        throw exception;
    }

    @Before("(${FieldSetNames}) && excludeAspectClasses()")
    //This method is used to log the field values before setting
    public void LogFieldBeforeSet(JoinPoint joinPoint) throws Exception {

        String fieldName = joinPoint.getSignature().getName();
        Object value = null;

        String className = null;


        value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
        className = joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();

        printLogMessage("Value of FIELD " + fieldName + " of CLASS " + className + " Before Setting is " + value);
    }

    @After("(${FieldSetNames}) && excludeAspectClasses()")
    //This method is used to log the field values after setting
    public void LogFieldAfterSet(JoinPoint joinPoint) throws Exception {

        String fieldName = joinPoint.getSignature().getName();
        Object value = null;
        String className = null;

        try {
            value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
            className = joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();

        } catch (Exception e) {
            e.printStackTrace();
        }

        printLogMessage("Value of FIELD " + fieldName + " of CLASS " + className + " After Setting is " + value);
    }

    @Before("(${FieldGetNames}) && excludeAspectClasses()")
    //This method is used to log the field values when accessed
    public void LogFieldGet(JoinPoint joinPoint) throws Exception {
        String fieldName = joinPoint.getSignature().getName();
        Object value = null;
        String className = null;

        value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
        className = joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();

        printLogMessage("Accessed FIELD " + fieldName + " " + value + " of CLASS " + className);
    }


    void printLogMessage(String message) {

        for (int i = 0; i < depth; i++) {
            writer.print("   ");
        }
        writer.println("|");
        for (int i = 0; i < depth; i++) {
            writer.print("   ");
        }
        writer.println("---> " + message);
        writer.flush();

    }

    String getTypeName(Object obj) {
        String type = null;
        if (obj instanceof int[]) {
            type = "Integer List";
        } else if (obj instanceof String[]) {
            type = "String List";
        } else if (obj instanceof char[]) {
            type = "Character List";
        } else if (obj instanceof boolean[]) {
            type = "Boolean List";
        } else if (obj instanceof double[]) {
            type = "Double List";
        } else if (obj instanceof float[]) {
            type = "Float List";
        } else if (obj instanceof long[]) {
            type = "Long List";
        } else if (obj instanceof short[]) {
            type = "Short List";
        }

        return type;
    }
}