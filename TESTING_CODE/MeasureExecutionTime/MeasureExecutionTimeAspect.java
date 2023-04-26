// ParallelizeAspect.java

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;


@Aspect
public class MeasureExecutionTimeAspect {

    @Around("execution(* MyService.*(..))")
    public Object wrap(final ProceedingJoinPoint point) throws Throwable{
        long start = System.currentTimeMillis();
        try {
            return point.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            System.out.println(point.getSignature() + " executed in " + executionTime + "ms");
        }
    }
}
