public class MyService {

    public int method1() {
        int a=4;
        return a;
    }


    public void method2() {
        try {
            int res1=method1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
