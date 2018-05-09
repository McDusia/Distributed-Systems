package Java;

import org.apache.thrift.TException;
import thrift.bank.InvalidArguments;
import thrift.bank.StandardAccountsService;


public class StandardAccountServiceHandler implements StandardAccountsService.Iface {

    private State state;

    StandardAccountServiceHandler(State state) {
        this.state = state;
    }

    @Override
    public double getAccountState(String GUID_number) throws TException {
        System.out.println("HELLLO");
        if(!state.existsGUID(GUID_number)) {
            throw new InvalidArguments(1, "wrong GUID number");
        }
        System.out.println("HELLLO");
        long PESEL = state.accounts.get(GUID_number);
        System.out.println(PESEL);
        BankClient client = state.people.get(PESEL);
        System.out.println(client);
        return client.getState();
    }
}
