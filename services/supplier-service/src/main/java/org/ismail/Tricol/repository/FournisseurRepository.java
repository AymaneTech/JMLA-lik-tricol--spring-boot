package org.ismail.Tricol.repository;

import org.ismail.Tricol.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    @Query("select f from Fournisseur f where f.nom = :nom")
    Fournisseur findByNom(@Param("nom") String nom);


    @Query("select f from Fournisseur f order by f.nom asc")
    List<Fournisseur> findAllOrderByNomAsc();
}
