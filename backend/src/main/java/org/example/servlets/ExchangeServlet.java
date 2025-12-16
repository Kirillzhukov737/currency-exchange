package src.main.java.org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import src.main.java.org.example.dto.ExchangeResultDTO;
import src.main.java.org.example.exception.GlobalExceptionHandler;


import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends CorsServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addCors(response);

        try {
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));

            BigDecimal result = exchangeService.exchange(from, to, amount);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(new ExchangeResultDTO(result)));
        } catch (Exception e) {
            GlobalExceptionHandler.handle(response, e);
        }
    }
}