package org.ismail.Tricol.service;

import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.repository.FournisseurRepository;

import java.util.List;
import java.util.Optional;

public class FournisseurServiceImp implements FournisseurService{

    private FournisseurRepository fournisseurRepository;

    public void setFournisseurRepository(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public void saveFournisseur(Fournisseur fournisseur) {
        fournisseurRepository.save(fournisseur);
    }

    @Override
    public List<Fournisseur> findAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    @Override
    public void deleteFournisseurById(Long id) {
        fournisseurRepository.deleteById(id);
    }

    @Override
    public Fournisseur updateFournisseur(Long id , Fournisseur fournisseur) {
        Fournisseur existingFournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur avec l'ID " + id + " non trouvé"));
        existingFournisseur.setId(id);
        existingFournisseur.setIce(fournisseur.getIce());
        existingFournisseur.setNom(fournisseur.getNom());
        existingFournisseur.setPrenom(fournisseur.getPrenom());
        existingFournisseur.setEmail(fournisseur.getEmail());
        existingFournisseur.setSociete(fournisseur.getSociete());
        existingFournisseur.setAdresse(fournisseur.getAdresse());
        existingFournisseur.setContact(fournisseur.getContact());
        existingFournisseur.setTelephone(fournisseur.getTelephone());
        existingFournisseur.setVille(fournisseur.getVille());

        return fournisseurRepository.save(existingFournisseur );
    }

    @Override
    public Optional<Fournisseur> findFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    @Override
    public Fournisseur findFournisseurByNom(String nom) {
        return fournisseurRepository.findByNom(nom);
    }

    @Override
    public List<Fournisseur> findAllFournisseursOrderByNomAsc() {
        return fournisseurRepository.findAllOrderByNomAsc();
    }
}