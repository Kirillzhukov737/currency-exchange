package src.main.java.org.example.dao.exchange;


import src.main.java.org.example.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAO {

    ExchangeRate save(ExchangeRate exchangeRate);

    Optional<ExchangeRate> findById(int id);

    Optional<ExchangeRate> findByCurrencyId(int baseCurrencyId, int targetCurrencyId);

    List<ExchangeRate> findAll();

    void updateRate(int id, ExchangeRate exchangeRate);
}