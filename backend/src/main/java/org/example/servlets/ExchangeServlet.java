package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangeResultDTO;
import org.example.exception.BadRequestException;
import org.example.service.ExchangeService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ExchangeService exchangeService() {
        return (ExchangeService) getServletContext().getAttribute("exchangeService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        BigDecimal amount = requireDecimal(request, "amount");

        ExchangeResultDTO result = exchangeService().exchange(from, to, amount);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    private static BigDecimal requireDecimal(HttpServletRequest request, String field) {
        String v = request.getParameter(field);
        if (v == null || v.isBlank()) {
            throw new BadRequestException("Missing required query param: " + field);
        }
        try {
            return new BigDecimal(v.trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid decimal value for field: " + field);
        }
    }
}