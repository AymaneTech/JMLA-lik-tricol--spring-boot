package org.ismail.gestiondesproduits.exception;

public class MouvementStockException extends RuntimeException {
    
    public MouvementStockException(String message) {
        super(message);
    }
    
    public MouvementStockException(String message, Throwable cause) {
        super(message, cause);
    }
}

