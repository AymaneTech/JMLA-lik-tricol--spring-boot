package org.ismail.gestiondescommmendsfournisseurspringboot.exception;

/**
 * Exception thrown when a Commande (Order) is not found in the database.
 * Provides a clear, domain-specific error for order lookup failures.
 */
public class CommandeNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Commande non trouvée avec l'id: %d";

    public CommandeNotFoundException(Long commandeId) {
        super(String.format(ERROR_MESSAGE_TEMPLATE, commandeId));
    }

    public CommandeNotFoundException(Long commandeId, Throwable cause) {
        super(String.format(ERROR_MESSAGE_TEMPLATE, commandeId), cause);
    }
}
