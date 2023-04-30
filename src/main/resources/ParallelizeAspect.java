// Importing AspectJ Packages & Java Parallel Packages

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

@Aspect
public class ParallelizeAspect {


@Pointcut("!within(ParallelizeAspect)")
  public void excludeParallelizeAspect() {
  }
//  @Pointcut("${MethodNames}")
//  public void includeMethod() {
//  }

  @Pointcut("@annotation(Parallelize) && execution(* *(..))")
  public void methodAnnotatedWithParallelize() {
  }
  @Around("methodAnnotatedWithParallelize() && excludeParallelizeAspect()")
  public Object wrap(final ProceedingJoinPoint point) {

    System.out.println("ParallelizeAspect.wrap()");
    final ExecutorService executor = Executors.newFixedThreadPool(1);

    final Class<?> returned = ((MethodSignature) point.getSignature()).getMethod().getReturnType();
    if (!Future.class.isAssignableFrom(returned)
            && !returned.equals(Void.TYPE)) {
      throw new IllegalStateException(
              String.format("Return type is %s, not void or Future, cannot use @Parallize",returned.getCanonicalName())
      );
    }
    final Future<?> result = executor.submit(
            () -> {
              Object ret = null;
              try {
                final Object res = point.proceed();
                if (res instanceof Future) {
                  ret = ((Future<?>) res).get();
                }
              } catch (final Throwable ex) {
                throw new IllegalStateException(
                        String.format("Exception thrown"),ex
                );
              }
              return ret;
            }
    );
    Object res = null;
    if (Future.class.isAssignableFrom(returned)) {
      res = result;
    }

    executor.shutdown();

    return res;
  }

}
