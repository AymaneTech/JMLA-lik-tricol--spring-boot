package com.ismail.authentification.exception;

public class IncorrectLoginCredentialsException extends RuntimeException {
    public IncorrectLoginCredentialsException(String message) {
        super(message);
    }
}
