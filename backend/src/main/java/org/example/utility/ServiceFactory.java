package src.main.java.org.example.utility;


import src.main.java.org.example.dao.currency.CurrencyDAOImpl;
import src.main.java.org.example.dao.exchange.ExchangeRateDAOImpl;
import src.main.java.org.example.service.*;

public final class ServiceFactory {
    private ServiceFactory() {
    }

    public static CurrencyService createCurrencyService(){
        return new CurrencyServiceImpl(new CurrencyDAOImpl());
    }
    public static ExchangeRateService createExchangeRateService() {
        return new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), new CurrencyDAOImpl());
    }
    public static ExchangeService createExchangeService() {
        return new ExchangeServiceImpl(new ExchangeRateDAOImpl(), new CurrencyDAOImpl());
    }
}