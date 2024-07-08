package com.vipin.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity handleUserAuthenticationException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(
                runtimeException.getMessage());
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity handleUserBlockedException(UserBlockedException userBlockedException){
        return ResponseEntity.badRequest().body(
                userBlockedException.getMessage());
    }
}