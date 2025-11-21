package org.ismail.gestiondesproduits.dto;

public record ProduitDTO(
    Long id,
    String nom,
    Double prix,
    String description,
    Integer quantite
) {}
