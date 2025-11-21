package org.ismail.Tricol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ismail.Tricol.model.Fournisseur;
import org.ismail.Tricol.service.FournisseurServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/fournisseurs")
@Tag(name = "Fournisseurs", description = "API de gestion des fournisseurs")
public class FournisseurController {

    private FournisseurServiceImp fournisseurServiceImp;

    public void setFournisseurService(FournisseurServiceImp fournisseurServiceImp) {
        this.fournisseurServiceImp = fournisseurServiceImp;
    }

    @Operation(summary = "Create Fournisseur", description = "Create a new supplier")
    @PostMapping
    public Fournisseur save(@RequestBody Fournisseur fournisseur) {
        fournisseurServiceImp.saveFournisseur(fournisseur);
        return fournisseur;
    }

    @Operation(summary = "Get All Fournisseurs", description = "Retrieve a list of all suppliers")
    @GetMapping
    public List<Fournisseur> findAll() {
        return fournisseurServiceImp.findAllFournisseurs();
    }

    @Operation(summary = "Get Sorted Fournisseurs", description = "Retrieve all suppliers sorted by name in ascending order")
    @GetMapping("/sorted")
    public List<Fournisseur> findAllOrderByNomAsc() {
        return fournisseurServiceImp.findAllFournisseursOrderByNomAsc();
    }


    @Operation(summary = "Find Fournisseur by Name", description = "Retrieve a supplier by their name")
    @GetMapping("/nom/{nom}")
    public Fournisseur findByNom(@PathVariable("nom") String nom) {
        return fournisseurServiceImp.findFournisseurByNom(nom);
    }

    @Operation(summary = "Delete Fournisseur", description = "Delete a supplier by ID")
    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable("id") Long id) {
        fournisseurServiceImp.deleteFournisseurById(id);
    }

    @Operation(summary = "Update Fournisseur", description = "Update an existing supplier by ID")
    @PutMapping("/{id}")
    public Fournisseur updateFournisseur(@PathVariable("id") Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurServiceImp.updateFournisseur(id, fournisseur);
    }

    @Operation(summary = "Find Fournisseur by ID", description = "Retrieve a supplier using their unique ID")
    @GetMapping("/{id}")
    public Fournisseur findById(@PathVariable("id") Long id) {
        return fournisseurServiceImp.findFournisseurById(id).orElseThrow(()-> new RuntimeException("Fournisseur not found with id " + id));
    }


}
