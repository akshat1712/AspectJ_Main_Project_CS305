// ParallelizeAspect.java

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


  @Pointcut("!within(MethodProfilerAspect)")
  public void excludeMethodProfilerAspect() {
  }
  @Around("${MethodNames} && excludeMethodProfilerAspect()")
  public Object wrap(final ProceedingJoinPoint point) throws Throwable {


    Object ret = null;
    try {

      // MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
      // MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
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

      String memoryUsed = methodName +  " Memory Used ( Heap ) in MB:" + (endMemory - startMemory) / factor;


      long elapsedProcessCpuTime = currProcessCpuTime - prevProcessCpuTime;
      long elapsedUpTime = currUpTime - prevUpTime;

      double cpuUsage = (elapsedProcessCpuTime / (elapsedUpTime * 1.0 * osBean.getAvailableProcessors())) * 100.0;



      printProfiler(memoryUsed);
      String temp = "cpuUsed";
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

  void printProfiler(String arg) {
    writer.println(arg);
    writer.flush();
  }
}
