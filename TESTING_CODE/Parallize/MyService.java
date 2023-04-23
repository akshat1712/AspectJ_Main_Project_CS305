import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public class MyService {

    @Parallize
    public Future<Integer> method1() {
        try {
            Thread.sleep(500);
            System.out.println("COMPLETED METHOD1 SLEEP");
            Future res = CompletableFuture.completedFuture(10);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Parallize
    public Future<Integer> method2() {
        try {
            Thread.sleep(1000);
            System.out.println("COMPLETED METHOD2 SLEEP");
            Future res = CompletableFuture.completedFuture(2);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Parallize
    public void method3() {
        try {
            long startTime = System.currentTimeMillis();
            Future<Integer> res1=method1();
            Future<Integer> res2=method2();
            
            System.out.println(res1.get() + res2.get());
            long endTime = System.currentTimeMillis();
            
            System.out.println("Execution time of " + "() took " + (endTime - startTime) + " ms.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}