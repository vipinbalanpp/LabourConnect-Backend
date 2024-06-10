package com.vipin.auth.exceptions;

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
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity handleUserAuthenticationException(UserAuthenticationException userAuthenticationException){
        return ResponseEntity.badRequest().body(
                userAuthenticationException.getMessage());
    }
    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity handleUserBlockedException(UserBlockedException userBlockedException){
        return ResponseEntity.badRequest().body(
                userBlockedException.getMessage());
    }
}