package com.vipin.notification.controller;

import com.vipin.notification.exception.InvalidEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity handleInvalidEmailException(InvalidEmailException invalidEmailException){
        return ResponseEntity.badRequest().body(
                invalidEmailException.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(
                runtimeException.getMessage());
    }
}
