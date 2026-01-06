package org.example.service;

import org.example.dao.currency.CurrencyDao;
import org.example.dao.exchange.ExchangeRateDao;
import org.example.dto.ExchangeResultDTO;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.mapper.CurrencyMapper;
import org.example.model.Currency;
import org.example.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private static final String USD = "USD";
    private static final int DIV_SCALE = 10;
    private static final int MONEY_SCALE = 2;

    private final ExchangeRateDao exchangeRateDAO;
    private final CurrencyDao currencyDAO;

    public ExchangeServiceImpl(ExchangeRateDao exchangeRateDAO, CurrencyDao currencyDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyDAO = currencyDAO;
    }

    @Override
    public ExchangeResultDTO exchange(String from, String to, BigDecimal amount) {
        String fromCode = normalizeCode(from, "from");
        String toCode = normalizeCode(to, "to");
        BigDecimal amt = normalizeAmount(amount);

        Currency fromCurrency = currencyDAO.findByCode(fromCode)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + fromCode));
        Currency toCurrency = currencyDAO.findByCode(toCode)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + toCode));

        BigDecimal rate = resolveRate(fromCurrency, toCurrency);

        BigDecimal converted = amt.multiply(rate).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal amountOut = amt.setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        return new ExchangeResultDTO(
                CurrencyMapper.toDto(fromCurrency),
                CurrencyMapper.toDto(toCurrency),
                rate,
                amountOut,
                converted
        );
    }

    private BigDecimal resolveRate(Currency from, Currency to) {
        if (from.id() == to.id()) {
            return BigDecimal.ONE;
        }

        Optional<ExchangeRate> direct = exchangeRateDAO.findByCurrencyId(from.id(), to.id());
        if (direct.isPresent()) {
            return direct.get().rate();
        }

        Optional<ExchangeRate> inverse = exchangeRateDAO.findByCurrencyId(to.id(), from.id());
        if (inverse.isPresent()) {
            BigDecimal r = inverse.get().rate();
            if (r.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Invalid exchange rate in DB");
            }
            return BigDecimal.ONE.divide(r, DIV_SCALE, RoundingMode.HALF_UP);
        }

        Currency usd = currencyDAO.findByCode(USD)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + USD));

        Optional<ExchangeRate> usdToFrom = exchangeRateDAO.findByCurrencyId(usd.id(), from.id());
        Optional<ExchangeRate> usdToTo = exchangeRateDAO.findByCurrencyId(usd.id(), to.id());

        if (usdToFrom.isPresent() && usdToTo.isPresent()) {
            BigDecimal rUsdA = usdToFrom.get().rate();
            BigDecimal rUsdB = usdToTo.get().rate();

            if (rUsdA.compareTo(BigDecimal.ZERO) <= 0 || rUsdB.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Invalid exchange rate in DB");
            }

            return rUsdB.divide(rUsdA, DIV_SCALE, RoundingMode.HALF_UP);
        }

        throw new NotFoundException("Exchange rate not found");
    }

    private static String normalizeCode(String code, String field) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Missing required query param: " + field);
        }
        String v = code.trim().toUpperCase();
        if (!v.matches("[A-Z]{3}")) {
            throw new BadRequestException("Invalid currency code: " + v);
        }
        return v;
    }

    private static BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null) throw new BadRequestException("Missing required query param: amount");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new BadRequestException("Amount must be positive");
        return amount;
    }
}
