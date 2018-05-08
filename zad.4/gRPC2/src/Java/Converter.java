package Java;

import Java.currencyExchange.CountedExchange;
import Java.currencyExchange.CurrencyExchangeGrpc;
import Java.currencyExchange.CurrencyType;
import Java.currencyExchange.ExchangeArguments;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Converter implements Runnable {
    private static final Logger logger = Logger.getLogger(Converter.class.getName());

    private final ManagedChannel channel;
    private final CurrencyExchangeGrpc.CurrencyExchangeBlockingStub curBlockingStub;
    private final CurrencyExchangeGrpc.CurrencyExchangeStub curNonBlockingStub;


    Converter(String host, int port)
    {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        curBlockingStub = CurrencyExchangeGrpc.newBlockingStub(channel);
        curNonBlockingStub = CurrencyExchangeGrpc.newStub(channel);
    }

    //public static void main(String[] args) throws Exception
    //{

    //}




    private void getCurrencyExchange() throws InterruptedException
    {
        try {
            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            do
            {
                try
                {
                    System.out.print("> ");
                    System.out.flush();
                    line = in.readLine();
                    if (line == null)
                    {
                        break;
                    }
                    if(line.equals("change"))
                    {
                        CurrencyType currencyType = CurrencyType.PLN;
                        ExchangeArguments request = ExchangeArguments.newBuilder().setCurrencyType(currencyType).build();
                        Iterator<CountedExchange> result;// = curBlockingStub.count(request);
                        try{
                            result = curBlockingStub.count(request);
                            while(result.hasNext()){
                                CountedExchange c = result.next();
                                System.out.println(c.getCurrencyTypeValue());
                                System.out.println(c.getRes()+ "\n");
                            }
                        } catch (StatusRuntimeException e) {
                            logger.warning("RPC failed");
                        }



                        /*StreamObserver<CountedExchange> responseObserver = new StreamObserver<CountedExchange>()
                        {
                            int count = -1;
                            @Override
                            public void onNext(CountedExchange result)
                            {
                                //tylko jeden wynik
                                count = result.getRes();
                            }

                            @Override
                            public void onError(Throwable t)
                            {
                                System.out.println("RPC ERROR");
                            }

                            @Override
                            public void onCompleted()
                            { System.out.println("Result received: converted " + count); }
                        };*/
                    }
                }
                catch (java.io.IOException ex)
                {
                    System.err.println(ex);
                }
            }
            while (!line.equals("exit"));
        } finally {
            shutdown();
        }
    }

    @Override
    public void run() {
        //Converter client = new Converter("localhost", 50051);

        try {
            getCurrencyExchange();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}

