package org.example.mapper;

import org.example.dto.CurrencyDTO;
import org.example.model.Currency;

public final class CurrencyMapper {

    private CurrencyMapper() {
    }

    public static CurrencyDTO toDto(Currency currency) {
        return new CurrencyDTO(currency.code(), currency.fullName(), currency.sign());
    }
}