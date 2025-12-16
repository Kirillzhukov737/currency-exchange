package org.example.service;

import org.example.dto.CurrencyCreateDTO;
import org.example.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    CurrencyDTO create(CurrencyCreateDTO dto);

    CurrencyDTO getByCode(String code);

    List<CurrencyDTO> getAll();
}