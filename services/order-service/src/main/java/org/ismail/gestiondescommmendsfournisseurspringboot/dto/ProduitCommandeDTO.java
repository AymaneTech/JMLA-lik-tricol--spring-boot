package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProduitCommandeDTO {
    private Long produitId;
    private Integer quantite;
}
