package org.ismail.mouvementstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ismail.mouvementstock.model.TypeMouvement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockRequestDTO {
    private Long produitId;
    private TypeMouvement typeMvt;
    private Integer quantite;
    private Double prixAchat;
    private Long refCommande;
}

