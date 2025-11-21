package org.ismail.Tricol.service;

import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.repository.FournisseurRepository;

import java.util.List;
import java.util.Optional;

public interface FournisseurService {

    void saveFournisseur(Fournisseur fournisseur);

    List<Fournisseur> findAllFournisseurs();

    void deleteFournisseurById(Long id);

    Fournisseur updateFournisseur(Long id , Fournisseur fournisseur);

    Optional<Fournisseur> findFournisseurById(Long id);

    Fournisseur findFournisseurByNom(String nom);

    List<Fournisseur> findAllFournisseursOrderByNomAsc();
}
