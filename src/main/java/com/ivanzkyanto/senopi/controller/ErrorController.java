package com.ivanzkyanto.senopi.controller;

import com.ivanzkyanto.senopi.model.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Void>builder().status("fail").message(e.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ApiResponse.<Void>builder()
                        .status("fail")
                        .message(e.getReason())
                        .build());
    }

}