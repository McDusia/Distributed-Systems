package Java;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import thrift.bank.AccountCreator;
import thrift.bank.PremiumAccountService;
import thrift.bank.StandardAccountsService;

public class BankServerConnectionHandler implements Runnable {

    private State state;
    BankServerConnectionHandler(State state){
        this.state = state;
    }

    //AccountCreator.Processor<AccountCreatorHandler>
    public static void start(TMultiplexedProcessor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);

            //TServer server = new TSimpleServer(
            //      new TServer.Args(serverTransport).processor(processor));

            //Use this for a multithreaded server
            //TServer server = new TThreadPoolServer(new
              //      TThreadPoolServer.Args(serverTransport).processor(processor));

            TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
            //TServer server = new TSimpleServer(new TServer.Args(serverTransport).protocolFactory(protocolFactory).processor(processor));
            //wielowątkowy serwer
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
                new PremiumAccountService.Processor<>(new PremiumAccountServiceHandler(state)));

        //new AccountCreator.Processor<>(new AccountCreatorHandler(state))
        start(processor1);
    }
}
