import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;


import java.lang.Exception;

@Aspect
public class LoggingAspect {


    static int depth=0;

//    PrintWriter writer;
//    public MethodLoggingAspect() {
//        try {
//                String logFileName = "${logFileName}";
//                if (logFileName != null && !logFileName.isEmpty()) {
//                    writer = new PrintWriter(new FileWriter(logFileName, true));
//                } else {
//                    writer = new PrintWriter(System.out);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        writer = new PrintWriter(System.out);
//    }


    @Before( "${MethodNames} && !within(LoggingAspect)" )
    public void LogMethodEntry(JoinPoint joinPoint) {

        String message;
        depth+=1;
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            message="Entering "+ joinPoint.getSignature().getName()+" With No arguments";
            printLogMessage(message);
            return;
        }

        message="Entering "+ joinPoint.getSignature().getName()+" With Following arguments";
        printLogMessage(message);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {

            String type=getTypeName(args[i]);

            if(type==null){
                message="Arguement "+ parameterNames[i]+": "+args[i];
                printLogMessage(message);
            }
            else {
                message = "Arguement " + parameterNames[i] + " of TYPE " + type;
                printLogMessage(message);
            }

        }

    }

    @AfterReturning( pointcut="${MethodNames} && !within(LoggingAspect)", returning = "result" )
    public void LogMethodReturn(JoinPoint joinPoint, Object result) {

        String message;
        message="Exiting "+joinPoint.getSignature().getName();
        printLogMessage(message);

        String type=getTypeName(result);

        if(type==null)
            message="Returned value of "+joinPoint.getSignature().getName()+": "+result;
        else
            message = "Returned value of " + joinPoint.getSignature().getName() + " of TYPE " + type;

        printLogMessage(message);

        depth-=1;
    }

    @AfterThrowing(pointcut="${MethodNames} && !within(LoggingAspect)", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Exception exception) throws Exception {

        String message= "Exception caught in "+joinPoint.getSignature().getName()+" of CLASS "+joinPoint.getTarget().getClass().getName();

        printLogMessage(message);

        depth-=1;
        throw exception;
    }

    @Before("${FieldSetNames} && !within(LoggingAspect)")
    public void LogFieldBeforeSet(JoinPoint joinPoint) {

        String fieldName = joinPoint.getSignature().getName();
        Object value = null;

        String className=null;

        try {
            value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
            className =joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printLogMessage("Value of FIELD " + fieldName + " of CLASS "+className+ " Before Setting is " + value);
    }

    @After("${FieldSetNames} && !within(LoggingAspect)")
    public void LogFieldAfterSet(JoinPoint joinPoint) {

        String fieldName = joinPoint.getSignature().getName();
        Object value = null;
        String className=null;

        try {
            value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
            className =joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();

        } catch (Exception e) {
            e.printStackTrace();
        }

        printLogMessage("Value of FIELD " + fieldName + " of CLASS "+className+ " After Setting is " + value);
    }

    @Before("${FieldGetNames} && !within(LoggingAspect)")
    public void LogFieldGet(JoinPoint joinPoint) {
        String fieldName = joinPoint.getSignature().getName();
        Object value = null;
        String className=null;
        try {
            value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
            className =joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();

        } catch (Exception e) {
            e.printStackTrace();
        }

        printLogMessage("Accessed FIELD " + fieldName + " " + value+" of CLASS "+className);
    }


    void printLogMessage(String message){

        for(int i=0;i<depth;i++){
            System.out.println("   ");
        }
        System.out.println("|");
        for(int i=0;i<depth;i++){
            System.out.println("   ");
        }
        System.out.println("---> "+message);
        System.out.flush();

    }

    String getTypeName(Object obj){
        String type=null;
        if( obj instanceof int[]){
            type="Integer List";
        }
        else if( obj instanceof String[]){
            type="String List";
        }
        else if( obj instanceof char[]){
            type="Character List";
        }
        else if( obj instanceof boolean[]){
            type="Boolean List";
        }
        else if( obj instanceof double[]){
            type="Double List";
        }
        else if( obj instanceof float[]){
            type="Float List";
        }
        else if( obj instanceof long[]){
            type="Long List";
        }
        else if( obj instanceof short[]){
            type="Short List";
        }

        return type;
    }
}
