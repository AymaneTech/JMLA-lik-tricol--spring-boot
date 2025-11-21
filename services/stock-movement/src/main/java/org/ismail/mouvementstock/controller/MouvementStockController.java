package org.ismail.mouvementstock.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.ismail.mouvementstock.service.MouvementStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mouvements")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    @Tag(name = "Create Mouvement", description = "Create a new stock movement (ENTREE, SORTIE, AJUSTEMENT)")
    @PostMapping
    public ResponseEntity<MouvementStockResponseDTO> createMouvement(@RequestBody MouvementStockRequestDTO requestDTO) {
        MouvementStockResponseDTO responseDTO = mouvementStockService.createMouvement(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Tag(name = "Get All Mouvements", description = "Retrieve a list of all stock movements")
    @GetMapping
    public ResponseEntity<List<MouvementStockResponseDTO>> getAllMouvements() {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getAllMouvements();
        return ResponseEntity.ok(mouvements);
    }

    @Tag(name = "Find Mouvement by ID", description = "Retrieve a stock movement using its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<MouvementStockResponseDTO> getMouvementById(@PathVariable Long id) {
        MouvementStockResponseDTO responseDTO = mouvementStockService.getMouvementById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Tag(name = "Get Mouvements by Product", description = "Retrieve all stock movements for a specific product")
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByProduit(@PathVariable Long produitId) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByProduitId(produitId);
        return ResponseEntity.ok(mouvements);
    }

    @Tag(name = "Get Mouvements by Type", description = "Retrieve all stock movements by type (ENTREE, SORTIE, AJUSTEMENT)")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByType(@PathVariable TypeMouvement type) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByType(type);
        return ResponseEntity.ok(mouvements);
    }

    @Tag(name = "Get Mouvements by Order", description = "Retrieve all stock movements for a specific order")
    @GetMapping("/commande/{refCommande}")
    public ResponseEntity<List<MouvementStockResponseDTO>> getMouvementsByCommande(@PathVariable Long refCommande) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getMouvementsByCommande(refCommande);
        return ResponseEntity.ok(mouvements);
    }

    @Tag(name = "Delete Mouvement", description = "Delete a stock movement by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        mouvementStockService.deleteMouvement(id);
        return ResponseEntity.noContent().build();
    }
}
