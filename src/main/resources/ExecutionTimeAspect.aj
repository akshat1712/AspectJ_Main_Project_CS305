import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import java.io.PrintWriter;
import java.io.FileWriter;

@Aspect
public class ExecutionTimeAspect {
    PrintWriter writer;
    public ExecutionTimeAspect() {
        try {
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

    @Around("${MethodNames}")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        writer.println("Execution time of " + joinPoint.getSignature().getName() + "() took " + (endTime - startTime) + " ms.");
        writer.flush();
        return result;
    }
}
