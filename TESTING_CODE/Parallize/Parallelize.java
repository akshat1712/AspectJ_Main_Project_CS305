// Importing Annotation Packages
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Defination @Parallelize Annotation to be applied at RunTime
// Applied to Methods Only
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parallelize {
}
