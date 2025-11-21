package org.ismail.gestiondescommmendsfournisseurspringboot.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/**
 * REST client for Product Service communication.
 * Handles all interactions with the product service API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProduitServiceClient {

    private static final String PRODUCTS_ENDPOINT = "/api/v1/products/";
    private static final String PRODUCT_FETCH_FAILED_MSG = "Impossible de récupérer le produit ID: {}";

    private final RestTemplate restTemplate;

    @Value("${produits.service.url}")
    private String produitsServiceUrl;

    /**
     * Fetches a product by its ID from the Product Service
     *
     * @param productId the ID of the product to fetch
     * @return an Optional containing the ProduitDTO if found, empty otherwise
     */
    public Optional<ProduitDTO> getProduct(Long productId) {
        try {
            ProduitDTO produit = restTemplate.getForObject(
                    buildProductUrl(productId),
                    ProduitDTO.class
            );
            return Optional.ofNullable(produit);
        } catch (RestClientException e) {
            log.error(PRODUCT_FETCH_FAILED_MSG, productId, e);
            return Optional.empty();
        }
    }

    /**
     * Builds the complete URL for fetching a product
     */
    private String buildProductUrl(Long productId) {
        return produitsServiceUrl + PRODUCTS_ENDPOINT + productId;
    }
}
