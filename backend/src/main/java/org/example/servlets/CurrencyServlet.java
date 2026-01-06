package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CurrencyDTO;
import org.example.exception.BadRequestException;
import org.example.service.CurrencyService;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CurrencyService currencyService() {
        return (CurrencyService) getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        if (path == null || path.equals("/") || path.isBlank()){
            throw new BadRequestException("Currency code is missing");
        }

        String code = path.substring(1);
        CurrencyDTO currency = currencyService().getByCode(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(currency));
    }
}