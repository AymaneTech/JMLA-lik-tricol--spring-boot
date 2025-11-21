package org.ismail.gestiondesfournisseursspringcore.exception;

public class FournisseurSaveException extends RuntimeException {
    
    public FournisseurSaveException(String message) {
        super(message);
    }
    
    public FournisseurSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

