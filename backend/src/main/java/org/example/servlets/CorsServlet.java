package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.CurrencyService;
import org.example.service.ExchangeRateService;
import org.example.service.ExchangeService;
import org.example.utility.ServiceFactory;

import java.io.IOException;

public abstract class CorsServlet extends HttpServlet {

    protected final CurrencyService currencyService = ServiceFactory.createCurrencyService();
    protected final ExchangeRateService exchangeRateService = ServiceFactory.createExchangeRateService();
    protected final ExchangeService exchangeService = ServiceFactory.createExchangeService();

    protected void addCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}