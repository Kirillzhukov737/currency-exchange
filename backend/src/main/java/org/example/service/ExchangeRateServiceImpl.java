package org.example.service;

import org.example.dao.currency.CurrencyDao;
import org.example.dao.exchange.ExchangeRateDao;
import org.example.dto.ExchangeRateCreateDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.dto.ExchangeRateUpdateDTO;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.mapper.ExchangeRateMapper;
import org.example.model.Currency;
import org.example.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateDao exchangeRateDAO;
    private final CurrencyDao currencyDAO;

    public ExchangeRateServiceImpl(ExchangeRateDao exchangeRateDAO, CurrencyDao currencyDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyDAO = currencyDAO;
    }

    @Override
    public ExchangeRateDTO create(ExchangeRateCreateDTO dto) {

        Currency baseCurrency = currencyDAO.findByCode(dto.baseCurrencyCode())
                .orElseThrow(() -> new NotFoundException("Base currency not found"));
        Currency targetCurrency = currencyDAO.findByCode(dto.targetCurrencyCode())
                .orElseThrow(() -> new NotFoundException("Target currency not found"));


        if (exchangeRateDAO.findByCurrencyId(baseCurrency.id(), targetCurrency.id()).isPresent()) {
            throw new ConflictException("Exchange rate already exists");
        }

        ExchangeRate rate = new ExchangeRate(0, baseCurrency.id(), targetCurrency.id(), dto.rate());
        ExchangeRate saved = exchangeRateDAO.save(rate);

        return ExchangeRateMapper.toDto(saved, baseCurrency, targetCurrency);
    }

    @Override
    public ExchangeRateDTO update(int id, ExchangeRateUpdateDTO dto) {

        ExchangeRate existing = exchangeRateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Exchange rate not found"));

        ExchangeRate updated = new ExchangeRate(
                existing.id(),
                existing.baseCurrencyId(),
                existing.targetCurrencyId(),
                dto.rate()
        );
        exchangeRateDAO.updateRate(id, updated);

        Currency baseCurrency = currencyDAO.findById(existing.baseCurrencyId())
                .orElseThrow(() -> new NotFoundException("Base currency not found"));
        Currency targetCurrency = currencyDAO.findById(existing.targetCurrencyId())
                .orElseThrow(() -> new NotFoundException("Target currency not found"));

        return ExchangeRateMapper.toDto(updated, baseCurrency, targetCurrency);
    }

    @Override
    public ExchangeRateDTO getByCodes(String baseCode, String targetCode) {

        Currency baseCurrency = currencyDAO.findByCode(baseCode)
                .orElseThrow(() -> new NotFoundException("Base currency not found"));
        Currency targetCurrency = currencyDAO.findByCode(targetCode)
                .orElseThrow(() -> new NotFoundException("Target currency not found"));

        ExchangeRate rate = exchangeRateDAO.findByCurrencyId(baseCurrency.id(), targetCurrency.id())
                .orElseThrow(() -> new NotFoundException("Exchange rate not found"));

        return ExchangeRateMapper.toDto(rate, baseCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRateDTO> getAll() {

        List<ExchangeRate> rates = exchangeRateDAO.findAll();
        List<ExchangeRateDTO> dtos = new ArrayList<>();

        for (ExchangeRate rate : rates) {
            Currency baseCurrency = currencyDAO.findById(rate.baseCurrencyId())
                    .orElseThrow(() -> new NotFoundException("Base currency not found"));
            Currency targetCurrency = currencyDAO.findById(rate.targetCurrencyId())
                    .orElseThrow(() -> new NotFoundException("Target currency not found"));

            dtos.add(ExchangeRateMapper.toDto(rate, baseCurrency, targetCurrency));
        }
        return dtos;
    }

    @Override
    public ExchangeRateDTO update(String baseCode, String targetCode, ExchangeRateUpdateDTO dto) {

        Currency baseCurrency = currencyDAO.findByCode(baseCode)
                .orElseThrow(() -> new NotFoundException("Base currency not found"));
        Currency targetCurrency = currencyDAO.findByCode(targetCode)
                .orElseThrow(() -> new NotFoundException("Target currency not found"));

        ExchangeRate existing = exchangeRateDAO.findByCurrencyId(baseCurrency.id(), targetCurrency.id())
                .orElseThrow(() -> new NotFoundException("Exchange rate not found"));

        ExchangeRate updated = new ExchangeRate(
                existing.id(),
                existing.baseCurrencyId(),
                existing.targetCurrencyId(),
                dto.rate()
        );

        exchangeRateDAO.updateRate(existing.id(), updated);

        return ExchangeRateMapper.toDto(updated, baseCurrency, targetCurrency);
    }
}