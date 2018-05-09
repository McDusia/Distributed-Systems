package Java;

import org.apache.thrift.TException;
import thrift.bank.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PremiumAccountServiceHandler extends StandardAccountServiceHandler implements PremiumAccountService.Iface{

    private State state;
    private Converter converter;

    PremiumAccountServiceHandler(State state, Converter converter) {
        super(state);
        this.state = state;
        this.converter = converter;
    }

    @Override
    public CreditCostsResult creditCosts(String GUID_number, CurrencyType currency, Period period) throws InvalidArguments {

        validateGUIDnumber(GUID_number);
        validateAccountType(GUID_number);

        long days = getDaysAmount(period);
        currencyExchange.CurrencyType currencyType = convertToGRPCCurrencyType(currency);
        Double exchangeRate = converter.getActualExchangeRate(currencyType);
        //sth better? TODO

        double inNativeCurrency = days * 1.0;
        double inForeignCurrency = (exchangeRate * days);

        CreditCostsResult result = new CreditCostsResult(inForeignCurrency, inNativeCurrency);
        double r1 = result.inRequiredCurrency;
        double r2 = result.inNativeCurrency;
        System.out.println(result);
        return result;
        //double nativeCurrency =  value *  getPeriosMonthsAmount(period)/10;
        //double forignCurrency = nativeCurrency * currentBankData().getCurrencyValue(currency_type);

    }

    private void validateGUIDnumber(String GUID_number) throws InvalidArguments {
        if (!state.existsGUID(GUID_number)) {
            throw new InvalidArguments(1, "[CREDIT COSTS]: bad GUID number");
        }
    }

    private void validateAccountType(String GUID_number) throws InvalidArguments {
        if (state.checkAccountType(GUID_number) == AccountType.STANDARD) {
            throw new InvalidArguments(1, "[CREDIT COSTS]: account with this GUID has standard type account");
        }
    }

    private long getDaysAmount(Period period) throws InvalidArguments {
        String startDateS = period.startDate;
        String endDateS = period.endDate;
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date startDate;
        Date endDate;
        try {
            startDate = format.parse(startDateS);
            endDate = format.parse(endDateS);
            return getDateDiff(startDate,endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            throw new InvalidArguments(3, "[CREDIT COSTS]: invalid data format in period arg");
        }
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    private currencyExchange.CurrencyType convertToGRPCCurrencyType(CurrencyType thriftCurrencyType){
        switch(thriftCurrencyType){
            case PLN:
                return currencyExchange.CurrencyType.PLN;
            case EUR:
                return currencyExchange.CurrencyType.EUR;
            case USD:
                return currencyExchange.CurrencyType.USD;
        } return null;
    }

}
