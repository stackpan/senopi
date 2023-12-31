package com.ivanzkyanto.senopi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRefreshTokenException extends ResponseStatusException {
    public InvalidRefreshTokenException() {
        super(HttpStatus.BAD_REQUEST, "Refresh token tidak valid");
    }
}
