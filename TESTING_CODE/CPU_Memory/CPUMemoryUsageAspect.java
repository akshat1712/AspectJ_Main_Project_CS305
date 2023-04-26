// ParallelizeAspect.java

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.sun.management.OperatingSystemMXBean;


import java.io.PrintWriter
@Aspect
public class CPUMemoryUsageAspect {
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
    @Around("@annotation(CPUMemoryUsage) && execution(* *(..))")
    public Object wrap(final ProceedingJoinPoint point) throws Throwable{

        Object ret = null;
        try {

            // MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            // MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();

            Runtime runtime = Runtime.getRuntime();
            System.gc();

            double startMemory= runtime.totalMemory() - runtime.freeMemory();

            int factor=1024*1024;
            ret = point.proceed();

            double endMemory= runtime.totalMemory() - runtime.freeMemory();

            System.gc();

            String memoryUsed = "Memory Used ( Heap ) in Mb:"+(endMemory-startMemory)/factor;
            printProfiler(memoryUsed);

            long elapsedProcessCpuTime = currProcessCpuTime - prevProcessCpuTime;
            long elapsedUpTime = currUpTime - prevUpTime;

            double cpuUsage = (elapsedProcessCpuTime / (elapsedUpTime * 1.0 * osBean.getAvailableProcessors())) * 100.0;

            String cpuUsed = "CPU Usage: " + String.format("%.5f", cpuUsage) + "%";
            printProfiler(cpuUsed);
            // System.out.println("Heap Memory Init: " + heapUsage.getInit()/factor + "  Mb");
            // System.out.println("Heap Memory Used: " + heapUsage.getUsed()/factor + "  Mb");
            // System.out.println("Heap Memory Committed: " + heapUsage.getCommitted()/factor + "  Mb");

            // MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
            // System.out.println("Non-Heap Memory Init: " + nonHeapUsage.getInit()/factor + "  Mb");
            // System.out.println("Non-Heap Memory Used: " + nonHeapUsage.getUsed()/factor + "  Mb");
            // System.out.println("Non-Heap Memory Committed: " + nonHeapUsage.getCommitted()/factor + "  Mb");

        } catch (Throwable e) {
            e.printStackTrace();
        }




        return ret;

    }

    void printProfiler(String arg){
        writer.println(arg);
        writer.flush();
    }
}
