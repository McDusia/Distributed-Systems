package Java;

import thrift.bank.AccountType;
import thrift.bank.Person;

public class BankClient {

    private String name;
    private String surname;
    private long PESEL;
    private int minEarnings;
    private AccountType accountType;
    private double state;

    BankClient(Person person) {
        this.name = person.name;
        this.surname = person.surname;
        this.PESEL = person.PESEL;
        this.minEarnings = person.minimalEarnings;

        int PREMIUM_THREASHOLD = 3000;

        if(this.minEarnings > PREMIUM_THREASHOLD){
            accountType = AccountType.PREMIUM;
        } else {
            accountType = AccountType.STANDARD;
        }
        state = 0;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public long getPESEL() {
        return PESEL;
    }

    public int getMinEarnings() {
        return minEarnings;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getState() {
        return state;
    }
}
