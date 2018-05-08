package Java;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer {

    public static State state = new State();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Runnable handler = new BankServerConnectionHandler(state);
        executor.execute(handler);
        Runnable converter = new Converter("localhost", 50051);
        executor.execute(converter);


    }
}
