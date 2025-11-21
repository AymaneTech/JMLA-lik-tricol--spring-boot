package org.ismail.gestiondescommmendsfournisseurspringboot.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * REST client for Stock Movement Service communication.
 * Handles recording stock movements when orders are delivered.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MouvementStockServiceClient {

    private static final String MOVEMENTS_ENDPOINT = "/api/v1/mouvements";
    private static final String MOVEMENT_RECORDED_MSG = "Mouvement de stock enregistré pour le produit ID: {}";
    private static final String MOVEMENT_FAILED_MSG = "Impossible d'enregistrer le mouvement pour le produit ID: {}";

    private final RestTemplate restTemplate;

    @Value("${mouvements.service.url}")
    private String mouvementsServiceUrl;

    /**
     * Records a stock movement in the Stock Movement Service
     *
     * @param produitId     the product ID
     * @param typeMouvement the type of movement (e.g., "SORTIE")
     * @param quantite      the quantity moved
     * @param prixAchat     the purchase price
     * @param refCommande   the reference order ID
     * @throws RestClientException if the API call fails
     */
    public void recordMovement(Long produitId, String typeMouvement, Integer quantite,
                               Double prixAchat, Long refCommande) throws RestClientException {
        try {
            Map<String, Object> mouvementData = buildMovementPayload(
                    produitId, typeMouvement, quantite, prixAchat, refCommande
            );

            restTemplate.postForObject(
                    buildMovementUrl(),
                    mouvementData,
                    Object.class
            );

            log.info(MOVEMENT_RECORDED_MSG, produitId);
        } catch (RestClientException e) {
            log.error(MOVEMENT_FAILED_MSG, produitId, e);
            throw e;
        }
    }

    /**
     * Builds the movement request payload
     */
    private Map<String, Object> buildMovementPayload(Long produitId, String typeMouvement,
                                                     Integer quantite, Double prixAchat,
                                                     Long refCommande) {
        return Map.of(
                "produitId", produitId,
                "typeMvt", typeMouvement,
                "quantite", quantite,
                "prixAchat", prixAchat,
                "refCommande", refCommande
        );
    }

    /**
     * Builds the complete URL for the movements endpoint
     */
    private String buildMovementUrl() {
        return mouvementsServiceUrl + MOVEMENTS_ENDPOINT;
    }
}
