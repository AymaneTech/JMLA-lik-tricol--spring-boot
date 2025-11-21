package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDetailDTO {
    private Long id;
    private String nom;
    private Double prix;
    private Integer quantite;
    private Double unitPrice;
}

