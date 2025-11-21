
package org.ismail.mouvementstock.exception;

public class MouvementStockNotFoundException extends RuntimeException {

    public MouvementStockNotFoundException(Long id) {
        super("Mouvement de stock non trouvé avec l'ID: " + id);
    }

    public MouvementStockNotFoundException(String message) {
        super(message);
    }
}
