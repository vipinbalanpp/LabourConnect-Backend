package com.example.booking.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleUserAuthenticationException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(
                runtimeException.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleUserEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        return ResponseEntity.badRequest().body(
                entityNotFoundException.getMessage());
    }
}
