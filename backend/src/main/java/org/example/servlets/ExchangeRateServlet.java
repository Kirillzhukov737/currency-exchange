package src.main.java.org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import src.main.java.org.example.dto.ExchangeRateCreateDTO;
import src.main.java.org.example.dto.ExchangeRateDTO;
import src.main.java.org.example.dto.ExchangeRateUpdateDTO;
import src.main.java.org.example.exception.GlobalExceptionHandler;


import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends CorsServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String[] codes = request.getPathInfo().substring(1).split("/");
            ExchangeRateDTO rate = exchangeRateService.getByCodes(codes[0], codes[1]);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(rate));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String base = request.getParameter("baseCurrencyCode");
            String target = request.getParameter("targetCurrencyCode");
            BigDecimal rate = new BigDecimal(request.getParameter("rate"));

            ExchangeRateCreateDTO dto = new ExchangeRateCreateDTO(base, target, rate);
            ExchangeRateDTO created = exchangeRateService.create(dto);

            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(created));

        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String pair = request.getPathInfo().substring(1);
            String[] codes = pair.split("/");

            String rateStr = request.getParameter("rate");
            BigDecimal newRate = new BigDecimal(rateStr);

            ExchangeRateUpdateDTO dto = new ExchangeRateUpdateDTO(newRate);
            ExchangeRateDTO updated = exchangeRateService.update(codes[0], codes[1], dto);

            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(updated));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
            return;
        }
        super.service(request, response);
    }
}