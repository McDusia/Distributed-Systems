package Java;


import currencyExchange.CurrencyType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer {

    public static State state = new State();

    public static void main(String[] args) {

        String wantedCurrencyType = null;
        CurrencyType currencyType = null;
        BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        int portNumber = 9090;
        try {
            System.out.print("Enter port number \n>");
            portNumber = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        while(currencyType == null)
        {
            System.out.print("Enter currency interesting for this bank.\n>");
            System.out.flush();
            try {
                wantedCurrencyType = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(wantedCurrencyType.equals("PLN")) {
                currencyType = CurrencyType.PLN;
            } else if (wantedCurrencyType.equals("USD")) {
                currencyType = CurrencyType.USD;
            } else if (wantedCurrencyType.equals("EUR")) {
                currencyType = CurrencyType.EUR;
            } else {
                System.out.println("We do not offer such currency type. Possible values: USD, EUR, PLN");
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Converter converter = new Converter("localhost", 50051, currencyType);
        executor.execute(converter);

        //int portNumber = 9090;
        Runnable handler = new BankServerConnectionHandler(state, converter, portNumber);
        executor.execute(handler);
    }
}
