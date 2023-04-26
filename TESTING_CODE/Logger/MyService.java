// MyService.java

public class MyService {


    public String name;
    public String email;

    public int[] method1( int num[],int a){

        name="JANJU";
        System.out.println("Method 1");
        return num;
    }

    public void method2(){
        email="JANJU2025@gmail.com";
        return;
    }

    public void method3() {
        name="John";
        email="123@gmail.com";
        
        int list[]= {1,2,3};

        list=method1(list,5);
        method2();

        try{
            method4();
        }
        catch(Exception e){
            // System.out.println("Exception caught");
        }

        name="AKSHAT";

        return;

    }

    public void method4() throws Exception{
        throw new Exception();
    }

}
