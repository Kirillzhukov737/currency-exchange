package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CurrencyCreateDTO;
import org.example.dto.CurrencyDTO;
import org.example.exception.GlobalExceptionHandler;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends CorsServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            List<CurrencyDTO> currencies = currencyService.getAll();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(currencies));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String sign = request.getParameter("sign");

            CurrencyCreateDTO dto = new CurrencyCreateDTO(code, name, sign);

            CurrencyDTO created = currencyService.create(dto);

            response.setContentType("application/json");
            response.getWriter().write(
                    objectMapper.writeValueAsString(created)
            );

        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }
}