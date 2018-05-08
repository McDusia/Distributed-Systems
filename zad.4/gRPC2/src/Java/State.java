package Java;

import thrift.bank.AccountType;
import thrift.bank.Person;

import java.util.HashMap;
import java.util.Map;

public class State {

    //pesel => person
    Map<Long, BankClient> people = new HashMap<>();
    //uuid => pesel
    Map<String, Long> accounts = new HashMap<>();


    public void createAccount(BankClient person, String GUID) {
        people.put(person.getPESEL(), person);
        accounts.put(GUID, person.getPESEL());
        seeState();  //can be commented
    }

    public boolean existsPESEL(Long PESEL){
        return people.containsKey(PESEL);
    }

    public boolean existsGUID(String GUID){
        return accounts.containsKey(GUID);
    }

    public AccountType checkAccountType(String GUID){
        Long PESEL = accounts.get(GUID);
        BankClient client = people.get(PESEL);
        return client.getAccountType();
    }

    private void seeState(){
        for (Long p: people.keySet()){
            String key =p.toString();
            BankClient value = people.get(p);
            String name = value.getName();
            String surname = value.getSurname();
            long minimalEarnings = value.getMinEarnings();
            System.out.println("NEW ACCOUNT CREATED: pesel: " + key + " name: " + name + " surname: " + surname
            + " minimalEarnings: " + minimalEarnings);
        }
    }


}
