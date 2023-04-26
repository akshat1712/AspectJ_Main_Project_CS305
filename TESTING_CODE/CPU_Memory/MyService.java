import java.util.ArrayList;
import java.lang.System;
public class MyService {

    String name,email;
    int id;
    double salary;

    ArrayList<String> code = new ArrayList<String>();
    
    public MyService() {
        name = "ABC";
        email = "abc@gmail.com";
        id = 1;
        salary = 1000.0;
        code.add("A");
    }

    public int method1( String s) {
        System.out.println("method1");
        code.add("B");
        email=s;
        return 1;
    }

    public int method2( int a) {
        System.out.println("method2");
        code.add("C");
        id=4;
        name="XYZ";
        salary+=a;
        return 2;
    }

    public int method3() throws Exception{
        code.remove(0);
        throw new RuntimeException("method3");
    }


    public void methodRunner() {
        try {
            int res1=method1( "10");
            int res2=method2( 12);
            try{
                method3();
            }
            catch(Exception e){
                System.out.println("Exception caught in method3");
            }

            System.out.println("res1:"+res1+" res2:"+res2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
