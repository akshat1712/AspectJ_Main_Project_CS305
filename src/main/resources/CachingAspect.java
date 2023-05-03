import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

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
  @Around("execution(Integer *(..)) && excludeAspectfile()")
  public Integer wrapInteger(final ProceedingJoinPoint point) {
    return integerCacheHandlerAspect.cachedToFunctionCalls(point);
  }




  //Around for methods which return String
  @Around("execution(String *(..)) && excludeAspectfile()")
  public String wrapString(final ProceedingJoinPoint point) {
    return stringCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return double
  @Around("execution(Double *(..)) && excludeAspectfile()")
  public Double wrapDouble(final ProceedingJoinPoint point) {
    return doubleCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return long
  @Around("execution(Long *(..)) && excludeAspectfile()")
  public Long wrapLong(final ProceedingJoinPoint point) {
    return longCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return boolean
  @Around("execution(Boolean *(..)) && excludeAspectfile()")
  public Boolean wrapBoolean(final ProceedingJoinPoint point) {
    return booleanCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return byte
  @Around("execution(Byte *(..)) && excludeAspectfile()")
  public Byte wrapByte(final ProceedingJoinPoint point) {
    return byteCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return char
  @Around("execution(Character *(..)) && excludeAspectfile()")
  public Character wrapChar(final ProceedingJoinPoint point) {
    return charCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return short
  @Around("execution(Short *(..)) && excludeAspectfile()")
  public Short wrapShort(final ProceedingJoinPoint point) {
    return shortCacheHandlerAspect.cachedToFunctionCalls(point);
  }

  //Around for methods which return float
  @Around("execution(Float *(..)) && excludeAspectfile()")
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

