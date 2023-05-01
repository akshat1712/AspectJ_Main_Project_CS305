package Testing.ParallelizeTest;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;


public class MyService {

    public Future<Integer> SumToN( ){

        int sum=0;
        for( int i=0;i<500;i++){
            sum+=i;
            System.out.println("Method 1 "+ i);
        }
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.complete(sum);
        return future;

    }

    public Future<Integer> SumToNSquare(){

        int sum=0;
        for(int i=0;i<500;i++){
            sum+=i*i;
            System.out.println("Method 2 "+ i);
        }
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.complete(sum);
        return future;

    }

    public Future<String> PrintList( int num[]){
        String list="";
        for(int i=0;i<num.length;i++){
            list+=num[i]+" ";
        }
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(list);
        return future;
    }
    
    public int test() {
        
        list=new int[100];
        for(int i=0;i<100;i++){
            list[i]=i;
        }

        Future<Integer> res1=SumToN();
        Future<Integer> res2=SumToNSquare();
        Future<String>  res3=PrintList(list);

        System.out.println("Result 1: "+res1.get());
        System.out.println("Result 2: "+res2.get());

        return res1.get()+res2.get();

    }

}
