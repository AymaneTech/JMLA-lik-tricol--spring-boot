package org.ismail.gestiondesproduits.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ismail.gestiondesproduits.dto.AddQuantityRequestDTO;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.dto.ReduceQuantityDTO;
import org.ismail.gestiondesproduits.mapper.ProduitMapper;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProduitController {

    @Autowired
    public ProduitService produitService;

    @Autowired
    public ProduitMapper produitMapper;

    @Tag(name = "Create Product", description = "Create a new product by providing product details")
    @PostMapping
    public Produit creatProduit(@RequestBody ProduitDTO p) {
        try {
            System.out.println(p);
            Produit pr = produitMapper.dtoToEntity(p);
            return produitService.save(pr);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du produit: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Tag(name = "Find Product by ID", description = "Retrieve a product using its unique ID")
    @GetMapping("/{id}")
    public Produit findById(@PathVariable("id") Long id) {
        return produitService.findById(id);
    }

    @Tag(name = "Get All Products", description = "Retrieve a list of all products")
    @GetMapping
    public List<Produit> findAll() {
        return produitService.findAllProduits();
    }

    @Tag(name = "Delete Product", description = "Delete a product by providing its details")
    @DeleteMapping
    public void delete(Produit p) {
        produitService.delete(p);
    }

    @Tag(name = "Update Product", description = "Update an existing product by providing the updated product details")
    @PutMapping
    public Produit update(Produit p) {

        return produitService.update(p);
    }

    @Tag(name = "Add Quantity", description = "Add quantity to an existing product and create an ENTREE movement")
    @PutMapping("/add-quantity/{id}")
    public ResponseEntity<Produit> addQuantity(
            @PathVariable("id") Long productId,
            @RequestBody AddQuantityRequestDTO request) {
        try {
            Produit updatedProduit = produitService.addQuantity(
                productId,
                request.getQuantityToAdd(),
                request.getPrixAchat()
            );
            return ResponseEntity.ok(updatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de quantité: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @Tag(name = "Reduce Quantity", description = "Reduce quantity from an existing product")
    @PatchMapping("/reduce-quantity/{id}")
    public boolean reduceQuantity(
            @PathVariable("id") Long productId,
            @RequestBody ReduceQuantityDTO request) {
            Produit updatedProduit = produitService.reduceQuantity(
                productId,
                request.getQuantityToReduce()
            );
            return updatedProduit != null;
    }
}
