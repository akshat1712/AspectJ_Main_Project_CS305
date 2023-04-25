import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public class MyService {

    @Parallize
    public Future<Integer> method1() {
        try {
            Thread.sleep(1000);
            int ans=0;
            for(int i=1;i<=200;i++){
                System.out.println("METHOD 1 :"+i);
                ans+=i;
            }
            Future res = CompletableFuture.completedFuture(ans);
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
            int ans=0;
            for(int i=1;i<=100;i++){
                System.out.println("METHOD 2 :"+i);
                ans+=i;
            }

            Future res = CompletableFuture.completedFuture(ans);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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