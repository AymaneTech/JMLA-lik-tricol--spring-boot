package org.ismail.mouvementstock.exception;

public class ProduitServiceException extends RuntimeException {
    
    public ProduitServiceException(String message) {
        super(message);
    }
    
    public ProduitServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

