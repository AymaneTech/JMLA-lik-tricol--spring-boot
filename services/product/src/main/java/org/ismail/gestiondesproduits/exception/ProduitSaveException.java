package org.ismail.gestiondesproduits.exception;

public class ProduitSaveException extends RuntimeException {
    
    public ProduitSaveException(String message) {
        super(message);
    }
    
    public ProduitSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

