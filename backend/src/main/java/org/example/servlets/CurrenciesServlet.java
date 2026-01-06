package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CurrencyCreateDTO;
import org.example.dto.CurrencyDTO;
import org.example.exception.BadRequestException;
import org.example.service.CurrencyService;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CurrencyService currencyService(){
        return (CurrencyService) getServletContext().getAttribute("currencyService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<CurrencyDTO> currencies = currencyService().getAll();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(currencies));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = requireForm(request, "code");
        String name = requireForm(request, "name");
        String sign = requireForm(request, "sign");

        CurrencyDTO created = currencyService().create(new CurrencyCreateDTO(code, name, sign));

        response.setStatus(HttpServletResponse.SC_CREATED);
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
}