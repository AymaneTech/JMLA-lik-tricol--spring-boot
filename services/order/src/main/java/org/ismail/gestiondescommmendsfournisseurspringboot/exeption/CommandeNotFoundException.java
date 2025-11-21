package org.ismail.gestiondescommmendsfournisseurspringboot.exeption;

public class CommandeNotFoundException extends RuntimeException {

    public CommandeNotFoundException(Long id) {
        super("Commande non trouvée avec l'ID: " + id);
    }

    public CommandeNotFoundException(String message) {
        super(message);
    }
}

