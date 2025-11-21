package org.ismail.mouvementstock.exception;

public class StockInsuffisantException extends RuntimeException {
    
    public StockInsuffisantException(Long produitId, Integer stockActuel, Integer quantiteDemandee) {
        super(String.format("Stock insuffisant pour le produit ID: %d. Stock actuel: %d, Quantité demandée: %d", 
            produitId, stockActuel, quantiteDemandee));
    }
    
    public StockInsuffisantException(String message) {
        super(message);
    }
}

