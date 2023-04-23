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

@Aspect
public class CPUMemoryUsageAspect {

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

            System.out.println("Memory Used ( Heap ) in Mb:"+(endMemory-startMemory)/factor);


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
}
