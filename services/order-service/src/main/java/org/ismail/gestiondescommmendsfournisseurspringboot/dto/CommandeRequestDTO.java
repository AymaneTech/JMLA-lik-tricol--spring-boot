package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.*;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequestDTO {
    private Long idFournisseur;
    private List<ProduitCommandeDTO> produits;
}

