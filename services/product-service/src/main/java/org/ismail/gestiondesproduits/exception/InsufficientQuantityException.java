package org.ismail.gestiondesproduits.exception;

public class InsufficientQuantityException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Quantité insuffisante pour le produit ID: %d. Disponible: %d, Demandé: %d";

    public InsufficientQuantityException(Long produitId, Integer available, Integer requested) {
        super(String.format(ERROR_MESSAGE, produitId, available, requested));
    }

    public InsufficientQuantityException(Long produitId, Integer available, Integer requested, Throwable cause) {
        super(String.format(ERROR_MESSAGE, produitId, available, requested), cause);
    }
}
