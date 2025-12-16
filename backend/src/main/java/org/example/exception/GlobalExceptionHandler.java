package src.main.java.org.example.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import src.main.java.org.example.dto.ErrorResponseDTO;


import java.io.IOException;

public class GlobalExceptionHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handle(HttpServletResponse response, Exception e) throws IOException {

        try {
            if (e instanceof NotFoundException) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            if (e instanceof IllegalArgumentException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage());

            response.getWriter().write(
                    objectMapper.writeValueAsString(error)
            );

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
