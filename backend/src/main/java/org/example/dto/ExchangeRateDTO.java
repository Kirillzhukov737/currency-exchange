package org.example.dto;

import java.math.BigDecimal;

public record ExchangeRateDTO(int id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, BigDecimal rate) {
}