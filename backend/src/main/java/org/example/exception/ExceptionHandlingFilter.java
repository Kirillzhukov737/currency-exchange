package org.example.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ErrorResponseDto;

import java.io.IOException;

@WebFilter(urlPatterns = {"/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"})
public class ExceptionHandlingFilter implements Filter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            write(resp, e.getStatus(), e.getMessage());
        } catch (IllegalArgumentException e) {
            write(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void write(HttpServletResponse servletResponse, int status, String message) throws IOException {
        servletResponse.setStatus(status);
        servletResponse.setContentType("application/json");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.getWriter().write(objectMapper.writeValueAsString(new ErrorResponseDto(message)));
    }
}