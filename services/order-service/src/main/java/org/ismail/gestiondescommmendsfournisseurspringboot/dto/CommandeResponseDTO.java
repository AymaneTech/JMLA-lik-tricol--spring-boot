package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import org.ismail.gestiondescommmendsfournisseurspringboot.enums.CommendeStatus;

import java.util.List;

public record CommandeResponseDTO(
        Long id,
        CommendeStatus status,
        Long idFournisseur,
        List<ProduitDetailDTO> produits
) {
}
