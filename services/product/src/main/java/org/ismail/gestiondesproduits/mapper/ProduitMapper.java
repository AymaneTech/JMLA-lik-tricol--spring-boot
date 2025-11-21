package org.ismail.gestiondesproduits.mapper;
import org.mapstruct.Mapper;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.model.Produit;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    Produit dtoToEntity(ProduitDTO source);
    ProduitDTO entityToDto(Produit destination);
}
