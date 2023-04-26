public class MyService {
    
    @Cached() // timeToLiveMillis = 10000
    public String method1() throws Exception{
        Thread.sleep(2000);
        // String returnValue= "return value of method1";
        // System.out.println(returnValue);
        // System.out.println("method1");
        return "return value of method1";
    }

    @Cached() // timeToLiveMillis = 10000
    public String method2() throws Exception{
        Thread.sleep(2000);
        // System.out.println("method2");
        // String returnValue= "return value of method2";
        // System.out.println(returnValue);
        return "return value of method2";
    }

    public String method3() {
        try {
            // System.out.println("method3.1");
            String res1=method1();
            String res2=method2();
            // System.out.println("method3.2");
            String returnValue = "m1:"+ res1 + " m2:" + res2;
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
