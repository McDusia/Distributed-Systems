package Java;

import org.apache.thrift.TException;
import thrift.bank.*;

public class PremiumAccountServiceHandler extends StandardAccountServiceHandler implements PremiumAccountService.Iface{

    private State state;

    PremiumAccountServiceHandler(State state) {
        super(state);
        this.state = state;
    }

    @Override
    public CreditCostsResult creditCosts(String GUID_number, CurrencyType currency, Period period) throws InvalidArguments, TException {
        return null;
    }
}
