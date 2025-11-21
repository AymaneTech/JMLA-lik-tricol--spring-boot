package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import java.util.List;

public record CommandeRequestDTO (
        Long idFournisseur,
        List<ProduitCommandeDTO> produits
){
}

