package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

public record ProduitDTO(
        Long id,
        String nom,
        Double prix,
        Integer quantite
) {
}
