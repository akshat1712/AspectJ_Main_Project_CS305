// Importing AspectJ Libraries

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import java.io.PrintWriter;
import java.io.FileWriter;

@Aspect
public class ExecutionTimeAspect {
    // Creating a PrintWriter object to write to a file
    PrintWriter writer;
    public ExecutionTimeAspect() {
        try {
                // f
                String logFileName = "${logFileName}";
                if (logFileName != null && !logFileName.isEmpty()) {
                    writer = new PrintWriter(new FileWriter(logFileName, true));
                } else {
                    writer = new PrintWriter(System.out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    // Creating a pointcut to match all methods given in the MethodNames variable
    @Around("${MethodNames}")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
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
