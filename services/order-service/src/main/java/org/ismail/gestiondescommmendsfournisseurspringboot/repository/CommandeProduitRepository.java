package org.ismail.gestiondescommmendsfournisseurspringboot.repository;

import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Long> {
    List<CommandeProduit> findByCommandeId(Long commandeId);
}
