package Java;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.bank.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {

        TTransport transport = null;
        try {

            BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            System.out.print("Enter port number to get connection with bank\n>");
            int portNumber = Integer.parseInt(in.readLine());

            transport = new TSocket("localhost", portNumber);
            TProtocol protocol = new TBinaryProtocol(transport, true, true);

            AccountCreator.Client service1 = new AccountCreator.Client(
                    new TMultiplexedProtocol(protocol, "AccountCreator"));
            StandardAccountsService.Client service2 = new StandardAccountsService.Client(
                    new TMultiplexedProtocol(protocol, "StandardAccountService"));
            PremiumAccountService.Client service3 = new PremiumAccountService.Client(
                    new TMultiplexedProtocol(protocol, "PremiumAccountService"));

            transport.open();

            String name = "Kinga";
            String surname = "Nowak";
            long PESEL = 61061916411L;
            int minimalEarnings = 4000;

            while(true){
                try {
                    System.out.println("CHOOSE SERVICE.\n 1 - create account \n 2 - get account state \n 3 - check credit costs");
                    System.out.print("----------------------------- \n >");
                    System.out.flush();
                    String op = in.readLine();
                    String GUID = null;
                    switch (op) {
                        case "1":
                            System.out.println("-----------------------------");
                            System.out.println("~ Welcome to the account creation service ~");
                            System.out.print("Enter name.\n>");
                            System.out.flush();
                            name = in.readLine();
                            System.out.print("Enter surname.\n>");
                            System.out.flush();
                            surname = in.readLine();
                            System.out.print("Enter PESEL.\n>");
                            System.out.flush();
                            PESEL = Long.parseLong(in.readLine());
                            System.out.print("Declare minimal earnings.\n>");
                            System.out.flush();
                            minimalEarnings = Integer.parseInt(in.readLine());
                            Person person = new Person(name, surname, PESEL, minimalEarnings);
                            String createdGUID = service1.createNewAccount(person);
                            System.out.println("your GUID number: " + createdGUID);
                            break;
                        case "2":
                            System.out.println("----------------------------- \n >");
                            System.out.println("~ Welcome to the account state checker service ~");
                            System.out.print("~ To log in to your account enter valid GUID number. ~ \n >");
                            System.out.flush();
                            GUID = in.readLine();
                            System.out.println("reeceived guid: " + GUID);
                            Double state = service2.getAccountState(GUID);
                            System.out.println("Your account state: " + state);
                            break;
                        case "3":
                            System.out.println("-----------------------------");
                            System.out.println("~ Welcome to the credit costs checker service ~");
                            System.out.print("~ To log in to your account enter valid GUID number. ~ \n >");
                            GUID = in.readLine();
                            System.out.print("Enter start date (format: \"January 2, 2018\")\n >");
                            String startDate = in.readLine();
                            System.out.print("Enter end date (format: \"January 2, 2018\")\n >");
                            String endDate = in.readLine();
                            Period period = new Period(startDate, endDate);
                            System.out.print("Enter currency \n >");
                            String c = in.readLine();

                            CreditCostsResult result = null;
                            switch (c) {
                                case "PLN":
                                    result = service3.creditCosts(GUID, CurrencyType.PLN, period);
                                    break;
                                case "EUR":
                                    result = service3.creditCosts(GUID, CurrencyType.EUR, period);
                                    break;
                                case "USD":
                                    result = service3.creditCosts(GUID, CurrencyType.USD, period);
                                    break;
                            }
                            if(result != null){
                                System.out.println(result.getInNativeCurrency());
                                System.out.println(result.getInRequiredCurrency());
                            }
                            break;
                        default:
                            System.out.println("No such option");
                    }
                } catch (InvalidArguments | IOException e) {
                    System.err.println(e);
                }
            }

        } catch (TTransportException e) {
            System.out.println("TTransportException");
            System.out.println("Check port");
            e.printStackTrace();
        } catch (TException e) {
            System.out.println("TException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } finally {
            if (transport != null){
                transport.close();
            }
        }
    }
}
