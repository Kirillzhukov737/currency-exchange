package src.main.java.org.example.service;


import src.main.java.org.example.dto.CurrencyCreateDTO;
import src.main.java.org.example.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    CurrencyDTO create(CurrencyCreateDTO dto);

    CurrencyDTO getByCode(String code);

    List<CurrencyDTO> getAll();
}