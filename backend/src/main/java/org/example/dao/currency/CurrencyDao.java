package org.example.dao.currency;

import org.example.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    List<Currency> findAll();

    Currency save(Currency currency);

    Optional<Currency> findById(int id);

    Optional<Currency> findByCode(String code);
}