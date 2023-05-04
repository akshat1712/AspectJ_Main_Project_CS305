import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Aspect
public class CachingAspect {

    GenericCacheHandlerAspect<Integer> integerCacheHandlerAspect = new GenericCacheHandlerAspect<Integer>();
    GenericCacheHandlerAspect<String> stringCacheHandlerAspect = new GenericCacheHandlerAspect<String>();
    GenericCacheHandlerAspect<Double> doubleCacheHandlerAspect = new GenericCacheHandlerAspect<Double>();
    GenericCacheHandlerAspect<Long> longCacheHandlerAspect = new GenericCacheHandlerAspect<Long>();
    GenericCacheHandlerAspect<Boolean> booleanCacheHandlerAspect = new GenericCacheHandlerAspect<Boolean>();
    GenericCacheHandlerAspect<Byte> byteCacheHandlerAspect = new GenericCacheHandlerAspect<Byte>();
    GenericCacheHandlerAspect<Character> charCacheHandlerAspect = new GenericCacheHandlerAspect<Character>();
    GenericCacheHandlerAspect<Short> shortCacheHandlerAspect = new GenericCacheHandlerAspect<Short>();
    GenericCacheHandlerAspect<Float> floatCacheHandlerAspect = new GenericCacheHandlerAspect<Float>();

    @Pointcut("!within(ParallelizeAspect) || !within(CachingAspect) || !within(ExecutionTimeAspect) || !within(LoggingAspect) || !within(MethodProfilerAspect)")
    public void excludeAspectClasses() {
    }

    @Around("(${MethodNamesInteger}) || (${MethodNamesint}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Integer or int
    public Integer wrapInteger(final ProceedingJoinPoint point) throws Exception {
        return integerCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesString}) && excludeAspectClasses()")
    //Mehod to cache the methods which return String
    public String wrapString(final ProceedingJoinPoint point) throws Exception {
        return stringCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesDouble}) || (${MethodNamesdouble}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Double or double
    public Double wrapDouble(final ProceedingJoinPoint point) throws Exception {
        return doubleCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesLong}) || (${MethodNameslong}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Long or long
    public Long wrapLong(final ProceedingJoinPoint point) throws Exception {
        return longCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesBoolean}) || (${MethodNamesboolean}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Boolean or boolean
    public Boolean wrapBoolean(final ProceedingJoinPoint point) throws Exception {
        return booleanCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesByte}) || (${MethodNamesbyte}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Byte or byte
    public Byte wrapByte(final ProceedingJoinPoint point) throws Exception {
        return byteCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesCharacter}) || (${MethodNameschar}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Character or char
    public Character wrapCharacter(final ProceedingJoinPoint point) throws Exception {
        return charCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesShort}) || (${MethodNamesshort}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Short or short
    public Short wrapShort(final ProceedingJoinPoint point) throws Exception {
        return shortCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    @Around("(${MethodNamesFloat}) || (${MethodNamesfloat}) && excludeAspectClasses()")
    //Mehod to cache the methods which return Float or float
    public Float wrapFloat(final ProceedingJoinPoint point) throws Exception {
        return floatCacheHandlerAspect.cachedToFunctionCalls(point);
    }


    private class GenericCacheHandlerAspect<T> {

        private Map<Integer, T> functionCallValues = new HashMap<>();

        public T cachedToFunctionCalls(ProceedingJoinPoint thisJoinPoint) throws Exception { // , cached.timeToLiveMillis()

            String name = thisJoinPoint.getSignature().getName();
            Object[] args = thisJoinPoint.getArgs();
            String keyString = name + Arrays.toString(args);
            Integer key = keyString.hashCode();

            if (functionCallValues.containsKey(key)) {
                T cachedValue = (T) functionCallValues.get(key);

                return cachedValue;
            }

            Object value = thisJoinPoint.proceed();
            T cachedValue = (T) value;
            functionCallValues.put(key, cachedValue);


            return (T) value;
        }

    }
}