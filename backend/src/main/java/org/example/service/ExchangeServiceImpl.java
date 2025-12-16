package src.main.java.org.example.service;


import src.main.java.org.example.dao.currency.CurrencyDAO;
import src.main.java.org.example.dao.exchange.ExchangeRateDAO;
import src.main.java.org.example.exception.NotFoundException;
import src.main.java.org.example.model.Currency;
import src.main.java.org.example.model.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeServiceImpl implements ExchangeService{

    private final ExchangeRateDAO exchangeRateDAO;
    private final CurrencyDAO currencyDAO;

    public ExchangeServiceImpl(ExchangeRateDAO exchangeRateDAO, CurrencyDAO currencyDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyDAO = currencyDAO;
    }

    @Override
    public BigDecimal exchange(String from, String to, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Currency fromCurrency = currencyDAO.findByCode(from)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + from));

        Currency toCurrency = currencyDAO.findByCode(to)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + to));

        ExchangeRate rate = exchangeRateDAO.findByCurrencyId(fromCurrency.id(), toCurrency.id())
                .orElseThrow(() -> new NotFoundException("Exchange rate not found: " + from + " - " + to
                ));
        return amount.multiply(rate.rate());
    }
}