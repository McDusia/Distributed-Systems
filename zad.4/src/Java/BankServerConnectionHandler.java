package Java;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import thrift.bank.AccountCreator;
import thrift.bank.PremiumAccountService;
import thrift.bank.StandardAccountsService;

public class BankServerConnectionHandler implements Runnable {

    private State state;
    private Converter converter;
    private int portNumber;

    BankServerConnectionHandler(State state, Converter converter, int portNumber){
        this.state = state;
        this.converter = converter;
        this.portNumber = portNumber;
    }

    public static void start(TMultiplexedProcessor processor, int portNumber) {
        try {
            TServerTransport serverTransport = new TServerSocket(portNumber);
            TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();

            //multithreaded server
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport)
                    .protocolFactory(protocolFactory).processor(processor));

            System.out.println("Starting the server on port 9090...");
            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        TMultiplexedProcessor processor1 = new TMultiplexedProcessor();

        processor1.registerProcessor(
                "AccountCreator",
                        new AccountCreator.Processor<>(new AccountCreatorHandler(state)));

        processor1.registerProcessor(
                "StandardAccountService",
                new StandardAccountsService.Processor<>(new StandardAccountServiceHandler(state)));

        processor1.registerProcessor(
                "PremiumAccountService",
                new PremiumAccountService.Processor<>(new PremiumAccountServiceHandler(state, converter)));

        start(processor1, portNumber);
    }
}
