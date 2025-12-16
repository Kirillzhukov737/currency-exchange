package src.main.java.org.example.service;


import src.main.java.org.example.dto.ExchangeRateCreateDTO;
import src.main.java.org.example.dto.ExchangeRateDTO;
import src.main.java.org.example.dto.ExchangeRateUpdateDTO;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRateDTO create(ExchangeRateCreateDTO dto);

    ExchangeRateDTO update(int id, ExchangeRateUpdateDTO dto);

    ExchangeRateDTO getByCodes(String baseCode, String targetCode);

    List<ExchangeRateDTO> getAll();

    ExchangeRateDTO update(String baseCode, String targetCode, ExchangeRateUpdateDTO dto);
}