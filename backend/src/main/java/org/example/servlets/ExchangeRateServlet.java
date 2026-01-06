package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangeRateDTO;
import org.example.dto.ExchangeRateUpdateDTO;
import org.example.exception.BadRequestException;
import org.example.service.ExchangeRateService;
import org.example.utility.FormData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ExchangeRateService exchangeRateService() {
        return (ExchangeRateService) getServletContext().getAttribute("exchangeRateService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pair = extractPair(request);
        String base = pair.substring(0, 3);
        String target = pair.substring(3, 6);

        ExchangeRateDTO dto = exchangeRateService().getByCodes(base, target);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pair = extractPair(request);
        String base = pair.substring(0, 3);
        String target = pair.substring(3, 6);

        String rateStr = request.getParameter("rate");

        if (rateStr == null || rateStr.isBlank()) {
            Map<String, String> form = FormData.readBodyForm(request);
            rateStr = form.get("rate");
        }

        if (rateStr == null || rateStr.isBlank()) {
            throw new BadRequestException("Missing required form field: rate");
        }

        BigDecimal rate;
        try {
            rate = new BigDecimal(rateStr.trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid decimal value for field: rate");
        }

        ExchangeRateDTO updated = exchangeRateService().update(base, target, new ExchangeRateUpdateDTO(rate));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(updated));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
            return;
        }
        super.service(request, response);
    }

    private static String extractPair(HttpServletRequest request) {

        String path = request.getPathInfo();
        if (path == null || path.equals("/") || path.isBlank()) {
            throw new BadRequestException("Currency pair codes are missing in path");
        }
        String pair = path.substring(1).trim().toUpperCase();
        if (!pair.matches("[A-Z]{6}")) {
            throw new BadRequestException("Currency pair must be 6 letters like USDRUB");
        }
        return pair;
    }
}
