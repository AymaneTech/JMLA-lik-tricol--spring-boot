package org.ismail.gestiondesfournisseursspringcore.exception;

public class InvalidFournisseurException extends RuntimeException {
    
    public InvalidFournisseurException(String message) {
        super(message);
    }
    
    public InvalidFournisseurException(String message, Throwable cause) {
        super(message, cause);
    }
}

