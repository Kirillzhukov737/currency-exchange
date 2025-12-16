package org.example.service;

import org.example.dao.currency.CurrencyDAO;
import org.example.dto.CurrencyCreateDTO;
import org.example.dto.CurrencyDTO;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.mapper.CurrencyMapper;
import org.example.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDAO currencyDAO;

    public CurrencyServiceImpl(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    @Override
    public CurrencyDTO create(CurrencyCreateDTO dto) {
        if (currencyDAO.findByCode(dto.code()).isPresent()) {
            throw new ConflictException("Currency already exists");
        }

        Currency currency = new Currency(0, dto.code(), dto.fullName(), dto.sign());
        Currency saved = currencyDAO.save(currency);

        return CurrencyMapper.toDto(saved);
    }

    @Override
    public CurrencyDTO getByCode(String code) {
        Currency currency = currencyDAO.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency not found"));

        return CurrencyMapper.toDto(currency);
    }

    @Override
    public List<CurrencyDTO> getAll() {
        List<Currency> currencies = currencyDAO.findAll();
        List<CurrencyDTO> dtos = new ArrayList<>();

        for (Currency c : currencies) {
            dtos.add(CurrencyMapper.toDto(c));
        }

        return dtos;
    }
}
