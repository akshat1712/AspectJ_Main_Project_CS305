// Importing AspectJ Libraries

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.FileWriter;
import java.io.PrintWriter;
@Aspect
public class ExecutionTimeAspect {
    // Creating a PrintWriter object to write to a file
    PrintWriter writer;
    public ExecutionTimeAspect() throws Exception{

        // f
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

    @Around("(${MethodNames}) && excludeAspectClasses()")
    //Method to measure the execution time of the methods
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Exception {
        // Getting the start time of the method
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // Proceeding with the method

        // Getting the end time of the method
        long endTime = System.currentTimeMillis();

        // Writing the execution time to the log file
        writer.println("Execution time of " + joinPoint.getSignature().getName() + "() took " + (endTime - startTime) + " ms.");
        writer.flush(); // Flushing the writer
        return result; // Returning the result of the method
    }
}