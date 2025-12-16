package org.example.service;

import java.math.BigDecimal;

public interface ExchangeService {

    BigDecimal exchange(String from, String to, BigDecimal amount);
}