package Testing.CacheTest;
public class Fibonacci {

    public int fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n-1) + fibonacci(n-2);
    }

    public void compute(int n) {
        int res=fibonacci(n);
        System.out.println("Fibonacci of "+n+" is "+res);
    }

}
