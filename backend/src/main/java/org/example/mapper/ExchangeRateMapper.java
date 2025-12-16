package org.example.mapper;

import org.example.dto.CurrencyDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.model.Currency;
import org.example.model.ExchangeRate;

public final class ExchangeRateMapper {

    private ExchangeRateMapper() {
    }

    public static ExchangeRateDTO toDto(ExchangeRate exchangeRate, Currency baseCurrency, Currency targetCurrency) {

        CurrencyDTO baseDto = CurrencyMapper.toDto(baseCurrency);
        CurrencyDTO targetDto = CurrencyMapper.toDto(targetCurrency);

        return new ExchangeRateDTO(exchangeRate.id(), baseDto, targetDto, exchangeRate.rate());
    }
}