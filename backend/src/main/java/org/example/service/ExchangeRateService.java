package org.example.service;

import org.example.dto.ExchangeRateCreateDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.dto.ExchangeRateUpdateDTO;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRateDTO create(ExchangeRateCreateDTO dto);

    ExchangeRateDTO update(int id, ExchangeRateUpdateDTO dto);

    ExchangeRateDTO getByCodes(String baseCode, String targetCode);

    List<ExchangeRateDTO> getAll();

    ExchangeRateDTO update(String baseCode, String targetCode, ExchangeRateUpdateDTO dto);
}