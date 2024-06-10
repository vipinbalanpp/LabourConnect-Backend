package com.example.user.excepation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleUserAuthenticationException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(
                runtimeException.getMessage());
    }
}
