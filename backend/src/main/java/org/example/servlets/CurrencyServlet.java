package src.main.java.org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import src.main.java.org.example.dto.CurrencyDTO;
import src.main.java.org.example.exception.GlobalExceptionHandler;


import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends CorsServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String code = request.getPathInfo().substring(1);
            CurrencyDTO currency = currencyService.getByCode(code);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(currency));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }
}