package org.example.model;

import java.math.BigDecimal;

public record ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
}
