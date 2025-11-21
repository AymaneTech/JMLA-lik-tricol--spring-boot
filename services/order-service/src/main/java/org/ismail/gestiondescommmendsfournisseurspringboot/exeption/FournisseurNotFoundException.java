package org.ismail.gestiondescommmendsfournisseurspringboot.exeption;

public class FournisseurNotFoundException extends RuntimeException {
    
    public FournisseurNotFoundException(Long id) {
        super("Fournisseur non trouvé avec l'ID: " + id);
    }
    
    public FournisseurNotFoundException(String message) {
        super(message);
    }
}

