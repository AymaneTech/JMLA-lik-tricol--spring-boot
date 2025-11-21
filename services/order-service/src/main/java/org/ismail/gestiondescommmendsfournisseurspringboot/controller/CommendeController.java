package org.ismail.gestiondescommmendsfournisseurspringboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeRequestDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.UpdateStatusRequest;
import org.ismail.gestiondescommmendsfournisseurspringboot.service.CommendeServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commandes")
@Tag(name = "Commandes", description = "API de gestion des commandes fournisseurs")
public class CommendeController {

    public final CommendeServiceImpl commendeServiceImpl;

    public CommendeController(CommendeServiceImpl commendeServiceImpl) {
        this.commendeServiceImpl = commendeServiceImpl;
    }

    @Operation(summary = "Create Commande", description = "Create a new order with multiple products using CUMP pricing method")
    @PostMapping
    public CommandeResponseDTO creerCommende(@RequestBody CommandeRequestDTO commandeRequest) {
        return commendeServiceImpl.creerCommende(commandeRequest);
    }

    @Operation(summary = "Find Commande by ID", description = "Retrieve an order using its unique ID with full product details")
    @GetMapping("/{id}")
    public CommandeResponseDTO findById(@PathVariable Long id) {
        return commendeServiceImpl.findById(id);
    }

    @Operation(summary = "Get All Commandes", description = "Retrieve a paginated list of all orders")
    @GetMapping
    public Page<CommandeResponseDTO> findAll(Pageable pageable) {
        return commendeServiceImpl.findAll(pageable);
    }

    @Operation(summary = "Delete Commande", description = "Delete an order by ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        commendeServiceImpl.deleteById(id);
    }

    @Operation(summary = "Update Commande Status", description = "Update order status (PENDING, CONFIRMED, DELIVERED, CANCELLED)")
    @PatchMapping("/{id}/status")
    public CommandeResponseDTO updateCommendeStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return commendeServiceImpl.updateCommendeStatus(id, request.status());
    }

}