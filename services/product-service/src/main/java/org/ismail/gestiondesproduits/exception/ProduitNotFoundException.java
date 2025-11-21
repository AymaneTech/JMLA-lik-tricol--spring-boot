package org.ismail.gestiondesproduits.exception;

public class ProduitNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Produit non trouvé avec l'ID: %d";

    public ProduitNotFoundException(Long produitId) {
        super(String.format(ERROR_MESSAGE, produitId));
    }

    public ProduitNotFoundException(Long produitId, Throwable cause) {
        super(String.format(ERROR_MESSAGE, produitId), cause);
    }
}
