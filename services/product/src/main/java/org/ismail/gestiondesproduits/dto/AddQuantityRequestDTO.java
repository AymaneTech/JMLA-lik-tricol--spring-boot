package org.ismail.gestiondesproduits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddQuantityRequestDTO {
    private Integer quantityToAdd;
    private Double prixAchat;
}

