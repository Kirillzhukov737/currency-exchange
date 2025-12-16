package src.main.java.org.example.mapper;


import src.main.java.org.example.dto.CurrencyDTO;
import src.main.java.org.example.model.Currency;

public final class CurrencyMapper {

    private CurrencyMapper() {
    }

    public static CurrencyDTO toDto(Currency currency) {
        return new CurrencyDTO(currency.code(), currency.fullName(), currency.sign());
    }
}