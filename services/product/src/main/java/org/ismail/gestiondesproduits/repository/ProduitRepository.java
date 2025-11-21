package org.ismail.gestiondesproduits.repository;

import org.ismail.gestiondesproduits.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
