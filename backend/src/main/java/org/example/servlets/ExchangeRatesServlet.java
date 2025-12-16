package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangeRateDTO;
import org.example.exception.GlobalExceptionHandler;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates/")
public class ExchangeRatesServlet extends CorsServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            List<ExchangeRateDTO> rates = exchangeRateService.getAll();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(rates));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }
}