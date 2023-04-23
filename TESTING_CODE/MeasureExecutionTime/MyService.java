public class MyService {

    @MeasureExecutionTime
    public int method1() {
        int a=0;
        for(int i=0;i<10000;i++){
            a+=i;
        }
        return a;
    }

    @MeasureExecutionTime
    public int method2() {
        int a=0;
        for(int i=0;i<100000;i++){
            a+=i;
        }
        return a;
    }

    public void method3() {
        try {
            int res1=method1();
            int res2=method2();
            System.out.println("res1:"+res1+" res2:"+res2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
