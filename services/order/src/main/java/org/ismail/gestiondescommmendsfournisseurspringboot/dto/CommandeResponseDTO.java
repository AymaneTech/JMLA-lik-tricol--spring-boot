package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.*;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponseDTO {
    private Long id;
    private CommendeStatus status;
    private Long idFournisseur;
    private List<ProduitDetailDTO> produits;
}
