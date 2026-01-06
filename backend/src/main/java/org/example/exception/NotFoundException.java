package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

public final class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }
}