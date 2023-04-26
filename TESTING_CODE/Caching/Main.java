public class Main {
    public static void main(String[] args) {
        MyService service = new MyService();
        try{
            System.out.println(service.method1());
            System.out.println(service.method2());
            // method 1, 2 values are cached
            System.out.println(service.method3());
        } catch (Exception e) {
            System.out.println("exception");
            e.printStackTrace();
        }
        return;
    }
}
