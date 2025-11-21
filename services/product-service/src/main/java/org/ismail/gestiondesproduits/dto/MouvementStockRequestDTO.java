package org.ismail.gestiondesproduits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockRequestDTO {
    private Long produitId;
    private String typeMvt;  // "ENTREE", "SORTIE", "AJUSTEMENT"
    private Integer quantite;
    private Double prixAchat;
    private Long refCommande;
}

