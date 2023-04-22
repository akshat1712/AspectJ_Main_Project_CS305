// LoggerAspect.java:

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.*;

@Aspect
public class LogAspect {

    static int depth=0;

    @Before("@annotation(Logger) && execution(* *(..))")
    public void LogMethodBefore(JoinPoint joinPoint) {
        depth+=1;

        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            printLogMessage("Entering "+ joinPoint.getSignature().getName()+" With No arguments");
            return;
        }

        printLogMessage("Entering "+ joinPoint.getSignature().getName()+" With Following arguments");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            
            System.out.println(args[i].getClass());

            if( args[i] instanceof Object[]){
                String type=null;

                if( args[i] instanceof String[]){
                    type="STRING";
                }
                else if( args[i] instanceof char[]){
                    type="CHAR";
                }
                else if( args[i] instanceof int[]){
                    type="INTEGER";
                }
                else if(args[i] instanceof float[]){
                    type="FLOAT";
                }
                else if( args[i] instanceof double[]){
                    type="DOUBLE";
                }
                else if(args[i] instanceof long[]){
                    type="LONG";
                }
                else if(args[i] instanceof boolean[]){
                    type="BOOLEAN";
                }
                else{
                    type="OBJECT";
                    // printLogMessage("Argument is List of  "+type+" of length "+((Object[])args[i]).length+" and contains following elements");
                    // for(int j=0;j<((Object[])args[i]).length;j++){
                    //     System.out.println(args[i][j]);
                    //     // printObjectFieldArgumentType(args[i][j],parameterNames[i]);
                    // }
                    continue;
                }

                System.out.println(1);
                System.out.println(type);

                printLogMessage("Argument is List of "+type+" of length "+((Object[])args[i]).length+" and contains following elements");
                for(int j=0;j<((Object[])args[i]).length;j++){
                    printLogMessage("Arguement "+ parameterNames[i]+"["+j+"]"+": "+((Object[])args[i])[j]);
                }
            }
            else{
                System.out.println(2);
                printLogMessage("Arguement "+ parameterNames[i]+": "+args[i]);
            }

        }

    }

    @AfterReturning(pointcut = "@annotation(Logger) && execution(* *(..))", returning = "result")
    public void LogMethodAfter(JoinPoint joinPoint, Object result) {

        printLogMessage("Exiting "+joinPoint.getSignature().getName());
        printLogMessage("Returned value of " + joinPoint.getSignature().getName() + ": " + result);
        depth-=1;
    }

    @Before("set(* MyService.* )")
    public void LogFieldBeforeSet(JoinPoint joinPoint) {

        String fieldName = joinPoint.getSignature().getName();
        Object value = null;
        
        // Get the class name of the object
        String className=null;

        try {
            value = joinPoint.getTarget().getClass().getDeclaredField(fieldName).get(joinPoint.getTarget());
            className =joinPoint.getTarget().getClass().getDeclaredField(fieldName).getDeclaringClass().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printLogMessage("FIELD " + fieldName + " Accessed of CLASS " + className);
        printLogMessage("Value of FIELD " + fieldName + " of CLASS "+className+ " Before Setting is " + value);
    }

    @After("set(* MyService.* )")
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

    @Before("get(* MyService.* )")
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
            System.out.print("   ");
        }

        System.out.println("|");

        for(int i=0;i<depth;i++){
            System.out.print("   ");
        }

        System.out.println("---> "+message);

    }


    // void printObjectFieldArgumentType( Object obj,String parameterName){
    //     printLogMessage("Object of class "+obj.getClass().getName()+" and contains following fields");
    //     Field[] fields = obj.getClass().getDeclaredFields();
    //     for (Field field : fields) {
    //         field.setAccessible(true);
    //         try {
    //             printLogMessage("Arguement "+ parameterName+"."+field.getName()+": "+field.get(obj));
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    
}
