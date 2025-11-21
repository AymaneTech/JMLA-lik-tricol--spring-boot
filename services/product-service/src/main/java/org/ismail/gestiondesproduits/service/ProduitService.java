package org.ismail.gestiondesproduits.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ismail.gestiondesproduits.exception.ProduitNotFoundException;
import org.ismail.gestiondesproduits.exception.InsufficientQuantityException;
import org.ismail.gestiondesproduits.model.Produit;
import org.ismail.gestiondesproduits.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProduitService {

    private static final String MOUVEMENT_CREATED_MSG = "Mouvement de stock créé pour le produit ID: {}";
    private static final String MOUVEMENT_SERVICE_ERROR = "Le service de mouvement de stock n'est pas accessible. Produit créé mais mouvement non enregistré.";
    private static final String PRODUCT_NOT_FOUND = "Produit non trouvé avec l'ID: {}";
    private static final String INSUFFICIENT_QUANTITY = "Quantité insuffisante pour le produit ID: {}. Disponible: {}, Demandé: {}";
    private static final String QUANTITY_REDUCED = "Quantité réduite pour le produit ID: {}";
    private static final String QUANTITY_ADDED = "Quantité ajoutée pour le produit ID: {}";
    private static final String ENTREE_MOVEMENT = "ENTREE";

    private final ProduitRepository produitRepository;
    private final RestClient restClient;

    @Value("${mouvements.service.url:http://localhost:8086/api/v1/mouvements}")
    private String mouvementsServiceUrl;

    public ProduitService(ProduitRepository produitRepository,
                          @Value("${mouvements.service.url:http://localhost:8086/api/v1/mouvements}") String mouvementsServiceUrl) {
        this.produitRepository = produitRepository;
        this.mouvementsServiceUrl = mouvementsServiceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(mouvementsServiceUrl)
                .build();
    }

    @Transactional
    public Produit save(Produit p) {
        Produit savedProduit = produitRepository.save(p);
        Double prix = calculUnitPrice(savedProduit, 0.0, 0);
        recordMouvement(savedProduit.id(), ENTREE_MOVEMENT, savedProduit.quantity(), prix, null);
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
        return produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException(id));
    }

    @Transactional
    public Produit addQuantity(Long productId, Integer quantityToAdd, Double prixAchat) {
        Produit produit = findById(productId);
        Double prix = calculUnitPrice(produit, prixAchat, quantityToAdd);
        Integer currentQuantity = produit.quantity() != null ? produit.quantity() : 0;

        produit.quantity(currentQuantity + quantityToAdd)
               .unitPrice(prix);

        Produit updatedProduit = produitRepository.save(produit);
        log.info(QUANTITY_ADDED, productId);
        return updatedProduit;
    }

    @Transactional
    public Produit reduceQuantity(Long productId, Integer quantityToReduce) {
        Produit produit = findById(productId);
        Integer currentQuantity = produit.quantity() != null ? produit.quantity() : 0;

        if (currentQuantity < quantityToReduce) {
            throw new InsufficientQuantityException(productId, currentQuantity, quantityToReduce);
        }

        produit.quantity(currentQuantity - quantityToReduce);
        Produit updatedProduit = produitRepository.save(produit);
        log.info(QUANTITY_REDUCED, productId);
        return updatedProduit;
    }

    private void recordMouvement(Long produitId, String typeMvt, Integer quantite, Double prixAchat, Long refCommande) {
        try {
            Map<String, Object> mouvementData = Map.of(
                    "produitId", produitId,
                    "typeMvt", typeMvt,
                    "quantite", quantite,
                    "prixAchat", prixAchat,
                    "refCommande", refCommande
            );

            restClient.post()
                    .uri("")
                    .body(mouvementData)
                    .retrieve()
                    .toBodilessEntity();

            log.info(MOUVEMENT_CREATED_MSG, produitId);
        } catch (RestClientException e) {
            log.warn(MOUVEMENT_SERVICE_ERROR);
        }
    }

    private Double calculUnitPrice(Produit produit, Double prixAchat, Integer quantityToAdd) {
        Double totalCurrentValue = produit.unitPrice() * produit.quantity();
        Double totalNewValue = prixAchat * quantityToAdd;
        Integer newTotalQuantity = produit.quantity() + quantityToAdd;

        return (totalCurrentValue + totalNewValue) / newTotalQuantity;
    }
}
