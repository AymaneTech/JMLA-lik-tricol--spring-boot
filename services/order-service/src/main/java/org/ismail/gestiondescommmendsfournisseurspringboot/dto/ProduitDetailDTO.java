package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

public record ProduitDetailDTO(
        Long id,
        String nom,
        Double prix,
        Integer quantite,
        Double unitPrice
) {
}

