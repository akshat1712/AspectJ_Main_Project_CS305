import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public class MyService {


    public int a;
    public int b;

    public int method1() {
        for(int i=0;i<100;i++){
            System.out.println("method1");
        }
        System.out.println("COMPLETED METHOD1 SLEEP");
        return 1;
    }


    public int method2() {
        for(int i=0;i<100;i++){
            System.out.println("method2");
        }
        System.out.println("COMPLETED METHOD2 SLEEP");
        return 3;
    }

    public void method3() {
        try {
            long startTime = System.currentTimeMillis();
            int res1=method1();
            int res2=method2();
            
            System.out.println(res1+ res2);
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("Execution time of " + "() took " + (endTime - startTime) + " ms.");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
