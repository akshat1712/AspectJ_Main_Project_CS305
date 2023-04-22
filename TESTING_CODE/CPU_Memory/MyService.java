

public class MyService {
    public int method1() {
        int a=0;
        for(int i=0;i<100000;i++){
            a+=i;
        }
        return a;
    }

    public int method2() {
        int a=0;
        for(int i=0;i<100000;i++){
            a+=i;
        }
        return a;
    }

    @CPUMemoryUsage
    public void method3() {
        try {
            int res1=method1();
            int res2=method2();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
