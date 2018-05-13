
namespace java thrift.bank

enum AccountType {
  PREMIUM = 0,
  STANDARD = 1
}

enum CurrencyType {
    PLN = 0,
    EUR = 1,
    USD = 2
}

typedef string GUID

//-----structs----------
struct Person {
    1: string name,
    2: string surname,
    3: i64 PESEL,
    4: i32 minimalEarnings
}

struct Period {
    1: string startDate,
    2: string endDate
}

struct CreditCostsResult {
    1: double inRequiredCurrency,
    2: double inNativeCurrency
}

//------exception--------
exception InvalidArguments {
   1: i32 whichArgument,
   2: string why
}

//-----services----------
service AccountCreator {
   GUID createNewAccount(1:Person person) throws (1: InvalidArguments ex)
}
service StandardAccountsService {
   double getAccountState(1:GUID GUID_number) throws (1: InvalidArguments ex)
}
service PremiumAccountService extends StandardAccountsService {
   CreditCostsResult creditCosts(1:GUID GUID_number, 2:CurrencyType currency, 3:Period period)
                                    throws (1: InvalidArguments ex)
}
