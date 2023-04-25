import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.*;

@Aspect
public class LogAspect {

    static int depth=0;

    // @Before("@annotation(LogMethod) && execution(* *(..))")
    @Before( "execution(* MyService.*(..))" )
    public void LogMethodEntry(JoinPoint joinPoint) {
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
            
            String type=getTypeName(args[i]);
            
            if(type==null)
                printLogMessage("Arguement "+ parameterNames[i]+": "+args[i]);            
            else
                printLogMessage("Arguement "+ parameterNames[i]+" of TYPE "+type);

        }

    }

    // @AfterReturning(pointcut = "@annotation(LogMethod) && execution(* *(..))", returning = "result")
    @AfterReturning( pointcut="execution(* MyService.*(..))", returning = "result" )
    public void LogMethodReturn(JoinPoint joinPoint, Object result) {

        printLogMessage("Exiting "+joinPoint.getSignature().getName());

        String type=getTypeName(result);

        if(type==null)
            printLogMessage("Returned value of " + joinPoint.getSignature().getName() + ": " + result);
        else
            printLogMessage("Returned value of " + joinPoint.getSignature().getName() + " of TYPE "+type);

        depth-=1;
    }

    // @AfterThrowing(pointcut = "@annotation(LogMethod) && execution(* *(..))", throwing = "ex")
    @AfterThrowing(pointcut="execution(* MyService.*(..))", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) throws Exception {
        
        String message= "Exception caught in "+joinPoint.getSignature().getName()+" of CLASS "+joinPoint.getTarget().getClass().getName();
        
        printLogMessage(message);

        depth-=1;
        throw ex;
    }

    // @Before("@annotation(LogVariable) && set(* *.*)")
    @Before("set(* MyService.*)")
    public void LogFieldBeforeSet(JoinPoint joinPoint) {

        // System.out.println(joinPoint.getSignature().getName());
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

    // @After("@annotation(LogVariable) && set(* *.*)")
    @After("set(* MyService.*)")
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

    // @Before("@annotation(LogVariable) && get(* *.*)")
    @Before("get(* MyService.*)")
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

    @Before("within(MyService) &&  execution(new(..))")
    public void logInstantiation( JoinPoint joinPoint ) {
        String className=joinPoint.getSignature().getDeclaringTypeName();

        printLogMessage("Instantiated CLASS: "+className);
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
