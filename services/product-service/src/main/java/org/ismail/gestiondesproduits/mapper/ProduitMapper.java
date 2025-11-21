package org.ismail.gestiondesproduits.mapper;

import org.mapstruct.Mapper;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.model.Produit;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    default Produit dtoToEntity(ProduitDTO source) {
        if (source == null) {
            return null;
        }
        return new Produit()
                .id(source.id())
                .name(source.nom())
                .unitPrice(source.prix())
                .description(source.description())
                .quantity(source.quantite());
    }

    default ProduitDTO entityToDto(Produit destination) {
        if (destination == null) {
            return null;
        }
        return new ProduitDTO(
                destination.id(),
                destination.name(),
                destination.unitPrice(),
                destination.description(),
                destination.quantity()
        );
    }
}
