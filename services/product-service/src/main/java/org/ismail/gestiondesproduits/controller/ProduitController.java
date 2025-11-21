package org.ismail.gestiondesproduits.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ismail.gestiondesproduits.dto.AddQuantityRequestDTO;
import org.ismail.gestiondesproduits.dto.ProduitDTO;
import org.ismail.gestiondesproduits.dto.ReduceQuantityDTO;
import org.ismail.gestiondesproduits.mapper.ProduitMapper;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;
    private final ProduitMapper produitMapper;

    @Tag(name = "Create Product", description = "Create a new product by providing product details")
    @PostMapping
    public Produit creatProduit(@RequestBody ProduitDTO p) {
        Produit pr = produitMapper.dtoToEntity(p);
        return produitService.save(pr);
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
        Produit updatedProduit = produitService.addQuantity(
                productId,
                request.quantiteAjouter(),
                request.prixAchat()
        );
        return ResponseEntity.ok(updatedProduit);
    }

    @Tag(name = "Reduce Quantity", description = "Reduce quantity from an existing product")
    @PatchMapping("/reduce-quantity/{id}")
    public ResponseEntity<Produit> reduceQuantity(
            @PathVariable("id") Long productId,
            @RequestBody ReduceQuantityDTO request) {
        Produit updatedProduit = produitService.reduceQuantity(
                productId,
                request.quantityToReduce()
        );
        return ResponseEntity.ok(updatedProduit);
    }
}
