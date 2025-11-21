package org.ismail.mouvementstock.exception;

public class InvalidMouvementException extends RuntimeException {
    
    public InvalidMouvementException(String message) {
        super(message);
    }
    
    public InvalidMouvementException(String message, Throwable cause) {
        super(message, cause);
    }
}

