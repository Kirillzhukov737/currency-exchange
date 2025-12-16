package src.main.java.org.example.dao.currency;

import src.main.java.org.example.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {

    List<Currency> findAll();

    Currency save(Currency currency);

    Optional<Currency> findById(int id);

    Optional<Currency> findByCode(String code);
}