package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

public final class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(HttpServletResponse.SC_CONFLICT, message);
    }
}