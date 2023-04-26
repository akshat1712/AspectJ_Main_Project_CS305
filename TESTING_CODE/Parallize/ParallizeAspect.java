// Importing AspectJ Packages & Java Parallel Packages

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

@Aspect
public class ParallizeAspect {

    @Around("@annotation(Parallelize) && execution(* *(..))")
    public Object wrap(final ProceedingJoinPoint point) {

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
