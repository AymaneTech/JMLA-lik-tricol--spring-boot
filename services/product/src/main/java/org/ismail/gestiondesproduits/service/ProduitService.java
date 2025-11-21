package org.ismail.gestiondesproduits.service;

import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProduitService {

    @Autowired
    public ProduitRepository produitRepository;
    private final RestClient restClient;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8086/api/v1/mouvements")
                .build();
    }
    @Transactional
    public Produit save(Produit p) {
        Produit savedProduit = produitRepository.save(p);
        Double prix =  calculUnitPrice(savedProduit , 0.0, 0);
        Map<String, Object> mouvementData = new HashMap<>();
        mouvementData.put("produitId", savedProduit.getId());
        mouvementData.put("typeMvt", "ENTREE");
        mouvementData.put("quantite", savedProduit.getQuantity());
        mouvementData.put("prixAchat", prix);
        mouvementData.put("refCommande", null);

        try {
            restClient.post()
                    .uri("")
                    .body(mouvementData)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("Mouvement de stock créé pour le produit ID: " + savedProduit.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du mouvement: " + e.getMessage());
            System.err.println("Le service de mouvement de stock n'est pas accessible. Le produit a été créé mais le mouvement n'a pas été enregistré.");
        }

        return savedProduit;
    }

    public void delete(Produit p) {
        produitRepository.delete(p);
    }

    public Produit update(Produit p) {
        return produitRepository.save(p);
    }

    public List<Produit> findAllProduits() {
        return produitRepository.findAll();
    }

    public Produit findById(Long id) {
        try {
            return produitRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Produit non trouvé");
        }
    }

    @Transactional
    public Produit addQuantity(Long productId, Integer quantityToAdd, Double prixAchat) {
        Produit produit = findById(productId);

        Double prix = calculUnitPrice(produit , prixAchat, quantityToAdd);

        if (produit == null) {
            throw new RuntimeException("Produit non trouvé avec l'ID: " + productId);
        }

        Integer currentQuantity = produit.getQuantity() != null ? produit.getQuantity() : 0;
        produit.setQuantity(currentQuantity + quantityToAdd);
        produit.setUnitPrice(prix);

        Produit updatedProduit = produitRepository.save(produit);


        return updatedProduit;
    }

    public Produit reduceQuantity(Long productId, Integer quantityToReduce) {
        Produit produit = findById(productId);
        if (produit == null) {
            throw new RuntimeException("Produit non trouvé avec l'ID: " + productId);
        }

        Integer currentQuantity = produit.getQuantity() != null ? produit.getQuantity() : 0;
        if (currentQuantity < quantityToReduce) {
            throw new RuntimeException("Quantité insuffisante pour le produit ID: " + productId);
        }

        produit.setQuantity(currentQuantity - quantityToReduce);
        return produitRepository.save(produit);
    }
    
    public Double calculUnitPrice(Produit produit , Double prixAchat, Integer quantityToAdd) {
        
        Double totalCurrentValue = produit.getUnitPrice() * produit.getQuantity();
        Double totalNewValue = prixAchat * quantityToAdd;
        Integer newTotalQuantity = produit.getQuantity() + quantityToAdd;

        return (totalCurrentValue + totalNewValue) / newTotalQuantity;
    }
}