package org.ismail.gestiondesproduits.dto;

public record AddQuantityRequestDTO(
    Integer quantiteAjouter,
    Double prixAchat
) {}
