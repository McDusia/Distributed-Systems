package Java;

import org.apache.thrift.TException;
import thrift.bank.AccountCreator;
import thrift.bank.InvalidArguments;
import thrift.bank.Person;


import static java.util.UUID.randomUUID;

public class AccountCreatorHandler implements AccountCreator.Iface{
    private State state;


    AccountCreatorHandler (State state) {
        this.state = state;
    }

    @Override
    public String createNewAccount(Person person) throws TException {
        validatePESEL(person.PESEL);
        validateNameAndSurname(person);

        String GUID = randomUUID().toString();
        state.createAccount(new BankClient(person), GUID);
        return GUID;
    }

    private void validateNameAndSurname(Person person) throws InvalidArguments{
        if(person.name.equals("")){
            throw new InvalidArguments(1, "empty name");
        }
        if(person.surname.equals("")) {
            throw new InvalidArguments(1, "empty surname");
        }
    }

    private void validatePESEL(long PESEL) throws InvalidArguments{
        if(state.existsPESEL(PESEL)){
            throw new InvalidArguments(3, "exists account with such PESEL");
        }
    }

}
