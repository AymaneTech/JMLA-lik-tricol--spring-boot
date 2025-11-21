package org.ismail.gestiondesproduits.exception;

public class InvalidQuantityException extends RuntimeException {
    
    public InvalidQuantityException(String message) {
        super(message);
    }
    
    public InvalidQuantityException(Integer quantity) {
        super("Quantité invalide: " + quantity + ". La quantité doit être positive.");
    }
}

