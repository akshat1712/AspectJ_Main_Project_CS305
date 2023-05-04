// Importing AspectJ Packages & Java Parallel Packages

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Aspect
public class ParallelizeAspect {


    @Pointcut("!within(ParallelizeAspect)")
    public void excludeParallelizeAspect() {
    }

    @Pointcut("${MethodNames}")
    public void methodAnnotatedWithParallelize() {
    }

    @Around("methodAnnotatedWithParallelize() && excludeParallelizeAspect()")
    public Object wrap(final ProceedingJoinPoint point) {

        final Class<?> returned = ((MethodSignature) point.getSignature()).getMethod().getReturnType();

        if (!Future.class.isAssignableFrom(returned)
                && !returned.equals(Void.TYPE)) {
            Object res = null;
            try {
                res = point.proceed();
            } catch (final Throwable ex) {
                throw new IllegalStateException(
                        String.format("Exception thrown"), ex
                );
            }

            return res;
        }

        final ExecutorService executor = Executors.newFixedThreadPool(1);

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
                                String.format("Exception thrown"), ex
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
