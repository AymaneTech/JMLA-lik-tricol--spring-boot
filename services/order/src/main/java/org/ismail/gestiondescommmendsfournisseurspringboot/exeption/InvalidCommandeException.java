package org.ismail.gestiondescommmendsfournisseurspringboot.exeption;

public class InvalidCommandeException extends RuntimeException {
    
    public InvalidCommandeException(String message) {
        super(message);
    }
    
    public InvalidCommandeException(String message, Throwable cause) {
        super(message, cause);
    }
}

