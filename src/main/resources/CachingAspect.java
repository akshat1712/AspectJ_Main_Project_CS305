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

    @Pointcut("!within(CachingAspect)")
    public void excludeAspectfile() {
    }

    //Around for methods which return Integer
    @Around("(${MethodNamesInteger}) || (${MethodNamesint}) && excludeAspectfile()")
    public Integer wrapInteger(final ProceedingJoinPoint point) {
        return integerCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return String
    @Around("(${MethodNamesString}) && excludeAspectfile()")
    public String wrapString(final ProceedingJoinPoint point) {
        return stringCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Double
    @Around("(${MethodNamesDouble}) || (${MethodNamesdouble}) && excludeAspectfile()")
    public Double wrapDouble(final ProceedingJoinPoint point) {
        return doubleCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Long
    @Around("(${MethodNamesLong}) || (${MethodNameslong}) && excludeAspectfile()")
    public Long wrapLong(final ProceedingJoinPoint point) {
        return longCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Boolean
    @Around("(${MethodNamesBoolean}) || (${MethodNamesboolean}) && excludeAspectfile()")
    public Boolean wrapBoolean(final ProceedingJoinPoint point) {
        return booleanCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Byte
    @Around("(${MethodNamesByte}) || (${MethodNamesbyte}) && excludeAspectfile()")
    public Byte wrapByte(final ProceedingJoinPoint point) {
        return byteCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Character
    @Around("(${MethodNamesCharacter}) || (${MethodNameschar}) && excludeAspectfile()")
    public Character wrapCharacter(final ProceedingJoinPoint point) {
        return charCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Short
    @Around("(${MethodNamesShort}) || (${MethodNamesshort}) && excludeAspectfile()")
    public Short wrapShort(final ProceedingJoinPoint point) {
        return shortCacheHandlerAspect.cachedToFunctionCalls(point);
    }

    //Around for methods which return Float
    @Around("(${MethodNamesFloat}) || (${MethodNamesfloat}) && excludeAspectfile()")
    public Float wrapFloat(final ProceedingJoinPoint point) {
        return floatCacheHandlerAspect.cachedToFunctionCalls(point);
    }


    private class GenericCacheHandlerAspect<T> {

        private Map<Integer, T> functionCallValues = new HashMap<>();

        public T cachedToFunctionCalls(ProceedingJoinPoint thisJoinPoint) { // , cached.timeToLiveMillis()

            String name = thisJoinPoint.getSignature().getName();
            Object[] args = thisJoinPoint.getArgs();
            String keyString = name + Arrays.toString(args);
            Integer key = keyString.hashCode();

            if (functionCallValues.containsKey(key)) { // functionCall
                T cachedValue = (T) functionCallValues.get(key); // functionCall

                return cachedValue;
            }

            Object value = thisJoinPoint.proceed();
            T cachedValue = (T) value;
            // , cached.timeToLiveMillis()
            functionCallValues.put(key, cachedValue); // functionCall


            return (T) value;
        }

    }

}

