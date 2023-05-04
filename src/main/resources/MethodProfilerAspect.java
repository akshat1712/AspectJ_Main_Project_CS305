import com.sun.management.OperatingSystemMXBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

@Aspect
public class MethodProfilerAspect {
    PrintWriter writer;

    public MethodProfilerAspect() {
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


    @Pointcut("!within(ParallelizeAspect) || !within(CachingAspect) || !within(ExecutionTimeAspect) || !within(LoggingAspect) || !within(MethodProfilerAspect)")
    public void excludeAspectClasses() {
    }

    @Around("(${MethodNames}) && excludeAspectClasses()")
    //This method is used to measure the cpu and memory usage of the methods
    public Object wrap(final ProceedingJoinPoint point) throws Throwable {


        Object ret = null;
        try {


            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            Runtime runtime = Runtime.getRuntime();
            System.gc();
            //initial time
            long prevProcessCpuTime = osBean.getProcessCpuTime();
            long prevUpTime = System.nanoTime();

            double startMemory = runtime.totalMemory() - runtime.freeMemory();

            ret = point.proceed();

            double endMemory = runtime.totalMemory() - runtime.freeMemory();

            //final time
            long currProcessCpuTime = osBean.getProcessCpuTime();
            long currUpTime = System.nanoTime();

            int factor = 1024 * 1024; //convert to MB
            String methodName = point.getSignature().toString();

            String memoryUsed = methodName + " Memory Used ( Heap ) in MB:" + (endMemory - startMemory) / factor;


            long elapsedProcessCpuTime = currProcessCpuTime - prevProcessCpuTime;
            long elapsedUpTime = currUpTime - prevUpTime;

            double cpuUsage = (elapsedProcessCpuTime / (elapsedUpTime * 1.0 * osBean.getAvailableProcessors())) * 100.0;


            printProfiler(memoryUsed);
            String temp = "cpuUsed";
            String cpuUsed = "CPU Usage: " + String.format("%.5f", cpuUsage) + "%";
            printProfiler(cpuUsed);


        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;

    }

    void printProfiler(String arg) {
        writer.println(arg);
        writer.flush();
    }
}