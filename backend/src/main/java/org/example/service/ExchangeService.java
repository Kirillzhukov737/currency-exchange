package org.example.service;

import org.example.dto.ExchangeResultDTO;

import java.math.BigDecimal;

public interface ExchangeService {

    ExchangeResultDTO exchange(String from, String to, BigDecimal amount);
}