package Java;

import currencyExchange.CountedExchange;
import currencyExchange.CurrencyExchangeGrpc;
import currencyExchange.CurrencyType;
import currencyExchange.ExchangeArguments;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Converter implements Runnable {
    private static final Logger logger = Logger.getLogger(Converter.class.getName());

    private final ManagedChannel channel;
    private final CurrencyExchangeGrpc.CurrencyExchangeBlockingStub curBlockingStub;
    private CurrencyType nativeCurrencyType;
    //conversion from currencyType to KEY of map : VALUE od map
    private Map<CurrencyType, Double> exchangeRate = new HashMap<>();


    Converter(String host, int port, CurrencyType currencyType)
    {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        curBlockingStub = CurrencyExchangeGrpc.newBlockingStub(channel);
        this.nativeCurrencyType = currencyType;
    }

    @Override
    public void run() {
        try {
            getCurrencyExchange();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Double getActualExchangeRate(CurrencyType toCurrencyType){
        if(exchangeRate.containsKey(toCurrencyType)){
            return exchangeRate.get(toCurrencyType);
        }
        else {
            System.out.println("ERROR: Such conversion impossible");
            return null;
        }
    }

    private void getCurrencyExchange() throws InterruptedException
    {
        ExchangeArguments request = ExchangeArguments.newBuilder().setCurrencyType(nativeCurrencyType).build();
        Iterator<CountedExchange> result;
        try{
            result = curBlockingStub.count(request);
            while(result.hasNext()){
                CountedExchange countedExchangeRate = result.next();
                updateExchangeRates(countedExchangeRate.getFromCurrencyType(),
                        countedExchangeRate.getToCurrencyType(), countedExchangeRate.getRes());
                seeMap();
            }
        } catch (StatusRuntimeException e) {
            logger.warning("RPC failed");
            shutdown();
        }
    }

    private void updateExchangeRates(CurrencyType fromCurrencyType, CurrencyType toCurrencyType, int value){
        if(fromCurrencyType == nativeCurrencyType) {
            Double valueToInsert = (double)value/10000;
            if(exchangeRate.containsKey(toCurrencyType)) {
                exchangeRate.replace(toCurrencyType, valueToInsert);
            } else {
                exchangeRate.put(toCurrencyType, valueToInsert);
            }
        }
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private void seeMap(){
        System.out.println("--------");
        for (Map.Entry<CurrencyType, Double> entry : exchangeRate.entrySet())
        {

            System.out.println("Conversion: "+ nativeCurrencyType.getValueDescriptor()+ " -> "
                    + entry.getKey() + " / " + entry.getValue());
        }
        System.out.println("--------");
    }
}

