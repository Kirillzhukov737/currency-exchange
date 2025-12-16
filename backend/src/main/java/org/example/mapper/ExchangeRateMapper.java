package src.main.java.org.example.mapper;


import src.main.java.org.example.dto.CurrencyDTO;
import src.main.java.org.example.dto.ExchangeRateDTO;
import src.main.java.org.example.model.Currency;
import src.main.java.org.example.model.ExchangeRate;

public final class ExchangeRateMapper {

    private ExchangeRateMapper() {
    }

    public static ExchangeRateDTO toDto(ExchangeRate exchangeRate, Currency baseCurrency, Currency targetCurrency) {

        CurrencyDTO baseDto = CurrencyMapper.toDto(baseCurrency);
        CurrencyDTO targetDto = CurrencyMapper.toDto(targetCurrency);

        return new ExchangeRateDTO(exchangeRate.id(), baseDto, targetDto, exchangeRate.rate());
    }
}