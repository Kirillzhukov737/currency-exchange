package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

public final class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}