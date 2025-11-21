package org.ismail.gestiondescommmendsfournisseurspringboot.repository;

import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommendeRepository extends JpaRepository<Commande, Long> {
}
