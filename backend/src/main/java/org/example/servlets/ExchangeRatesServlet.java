package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangeRateCreateDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.exception.BadRequestException;
import org.example.service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ExchangeRateService exchangeRateService() {
        return (ExchangeRateService) getServletContext().getAttribute("exchangeRateService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRateDTO> rates = exchangeRateService().getAll();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(rates));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String base = requireForm(request, "baseCurrencyCode");
        String target = requireForm(request, "targetCurrencyCode");
        BigDecimal rate = requireDecimal(request, "rate");

        ExchangeRateDTO created = exchangeRateService().create(new ExchangeRateCreateDTO(base, target, rate));

        response.setStatus(HttpServletResponse.SC_CREATED); // 201 по ТЗ
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(created));
    }

    private static String requireForm(HttpServletRequest request, String field) {
        String v = request.getParameter(field);
        if (v == null || v.isBlank()) {
            throw new BadRequestException("Missing required form field: " + field);
        }
        return v.trim();
    }

    private static BigDecimal requireDecimal(HttpServletRequest request, String field) {
        String v = requireForm(request, field);
        try {
            return new BigDecimal(v);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid decimal value for field: " + field);
        }
    }
}
