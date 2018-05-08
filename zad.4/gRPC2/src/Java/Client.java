package Java;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.bank.*;

public class Client {
    public static void main(String[] args) {

        try{
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            //TProtocol protocol = new TBinaryProtocol(transport);
            TProtocol protocol = new TBinaryProtocol(transport, true, true);
            //synCalc1 = new Calculator.Client(new TMultiplexedProtocol(protocol, "S1"));
            //AccountCreator.Client client = new AccountCreator.Client(protocol);
            AccountCreator.Client client1 = new AccountCreator.Client(
                    new TMultiplexedProtocol(protocol, "AccountCreator"));
            StandardAccountsService.Client client2 = new StandardAccountsService.Client(
                    new TMultiplexedProtocol(protocol, "StandardAccountService"));
            PremiumAccountService.Client client3 = new PremiumAccountService.Client(
                    new TMultiplexedProtocol(protocol, "PremiumAccountService"));

            String name = "Kinga";
            String surname = "Nowak";
            long PESEL = 96061956411L;
            int minimalEarnings = 2000;

            Person person = new Person(name, surname, PESEL, minimalEarnings); //TODO
            String GUID = client1.createNewAccount(person);
            System.out.println("my GUID: " + GUID);
            double state = client2.getAccountState(GUID);
            System.out.println("Account state: " + state);
            //TODO
            //CreditCostsResult costs = client3.creditCosts(GUID, CurrencyType.EUR, new Period("start", "end") );
            //costs.inNative;
            //costs.inRequiredCurrency

            transport.close();

        } catch (TTransportException e) {
            System.out.println("TTransportException");
            e.printStackTrace();
        } catch (TException e) {
            System.out.println("TException");
            e.printStackTrace();
        }
    }
}
