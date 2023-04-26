import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.Arrays;

@Aspect
public class StringCachingAspect {
    // Map stores values of string returning function calls
    // private Map<NewFunctionCall, CachedValue> stringFunctionCallValues = new HashMap<NewFunctionCall, CachedValue>();
    private Map<Integer, CachedValue> stringFunctionCallValues = new HashMap<Integer, CachedValue>();


    // This pointcut matches any call to a method that returns a String and is annotated
    @Pointcut("execution(@Cached public String *.*(..)) && @annotation(Cached)")
    public void cachedPointCut() {} // Cached cached

    // We'll use around advice to replace above matched function calls with cache
    // checking, where we see if a non-expired value is already available
    // in the cache and return that in lieu of calling the method.
    @Around(value = "cachedPointCut()") // , argNames = "cached"
    public String cachedToStringCalls(ProceedingJoinPoint thisJoinPoint) { // , cached.timeToLiveMillis()

        String name = thisJoinPoint.getSignature().getName();
        Object[] args = thisJoinPoint.getArgs();
        String keyString = name + Arrays.toString(args);
        Integer key = keyString.hashCode();

        // NewFunctionCall functionCall = new NewFunctionCall(name, args);

        // If there is already a value in the cache for the NewFunctionCall in
        // question, check if it has expired; if not, return it.
        if (stringFunctionCallValues.containsKey(key)) { // functionCall
            CachedValue cachedValue = stringFunctionCallValues.get(key); // functionCall
            System.out.println("Key exists, taken from cache");

            // if (cachedValue.expirationTimeMillis <= System.currentTimeMillis()) {
            //     return cachedValue.value;
            // }

            return cachedValue.value;
        }

        // If there is no value in the cache, or if the value in the
        // cache has expired, then we'll need to call the original
        // method and save the result in the cache before returning it.
        Object value = thisJoinPoint.proceed();
        CachedValue cachedValue = new CachedValue((String) value);
        // , cached.timeToLiveMillis()
        stringFunctionCallValues.put(key, cachedValue); // functionCall
        System.out.println("Key does not exist, put in cache");

        for (Map.Entry<Integer, CachedValue> entry : stringFunctionCallValues.entrySet()) {
            Integer key1 = entry.getKey();
            CachedValue cachedValue1 = entry.getValue();
            System.out.println(key + " -> " + cachedValue1);
        }

        return (String) value;
    }

    // The CachedValue class combines a value with its expiration time,
    // expressed as a number of milliseconds since the Unix epoch -- the
    // same way that Java expresses the current time internally.

    // This is the value in the HashMap: a cached string value and its expiration time.
    private static class CachedValue {
        public String value;
        // public long expirationTimeMillis;

        // When we create a CachedValue, we'll take the time to live and
        // add it to the current time to calculate its expiration time.
        public CachedValue(String value) { // , int timeToLiveMillis
            this.value = value;

            // expirationTimeMillis =
            //     System.currentTimeMillis() + timeToLiveMillis;
        }
    }

    private class NewFunctionCall {
    
        private String name;
        private Object[] args;
    
        public NewFunctionCall(String name, Object[] args) {
            this.name = name;
            this.args = args;
        }
    }
}

