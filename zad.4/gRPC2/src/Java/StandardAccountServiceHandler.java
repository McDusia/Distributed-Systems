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
        if(!state.existsGUID(GUID_number)) {
            throw new InvalidArguments(1, "wrong GUID number");
        }
        long PESEL = state.accounts.get(GUID_number);
        BankClient client = state.people.get(PESEL);
        return client.getState();
    }
}
