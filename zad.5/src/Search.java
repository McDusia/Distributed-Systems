
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Search {
    public static void main(String[] args) {


        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> fileReader_1 = new DB_Reader("src\\DB_1", "Noelka");
        Callable<String> fileReader_2 = new DB_Reader("src\\DB_2", "Noelka");

        List<Callable<String>> callableList = new ArrayList<>();
        callableList.add(fileReader_1);
        callableList.add(fileReader_2);

        //TODO teraz dostaje wszystkie odpowiedzi
        try {
            List<Future<String>> futures = executor.invokeAll(callableList);

            for(Future<String> future : futures){
                    System.out.println("future.isDone = " + future.isDone());
                    System.out.println("future: call ="+future.get());
            }


            //String result = executor.invokeAny(callableList);
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
        }

        executor.shutdown();


    }


}
