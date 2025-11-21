package com.ismail.authentification.controller;

import com.ismail.authentification.exception.IncorrectLoginCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectLoginCredentialsException.class)
    ResponseEntity<String> handleIncorrectLoginCredentials(IncorrectLoginCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> exception(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur de serveur: " + ex.getMessage());
    }
}
