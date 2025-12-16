package org.example.dto;

import java.math.BigDecimal;

public record ExchangeRateCreateDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
}