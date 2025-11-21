package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ismail.gestiondescommmendsfournisseurspringboot.client.MouvementStockServiceClient;
import org.ismail.gestiondescommmendsfournisseurspringboot.client.ProduitServiceClient;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeRequestDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitCommandeDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.ProduitDetailDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.enums.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.exception.CommandeNotFoundException;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommandeProduitRepository;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommendeServiceImpl implements CommendeService {
    private static final String PRODUIT_NOT_FOUND_MSG = "Erreur lors de la récupération du produit ID: {}";
    private static final String COMMANDE_NOT_FOUND_MSG = "Commande non trouvée avec l'id: {}";
    private static final String REDUCTION_SUCCESS_MSG = "Quantité réduite pour le produit ID: {}";
    private static final String MOVEMENT_SUCCESS_MSG = "Mouvement de stock SORTIE créé pour le produit ID: {}";
    private static final String MOVEMENT_SERVICE_ERROR_MSG = "Le service de mouvement n'est pas disponible (optionnel): {}";
    private static final String REDUCTION_ERROR_MSG = "Erreur lors de la réduction de quantité pour le produit ID: {}";
    private static final String SORTIE_MOVEMENT_TYPE = "SORTIE";
    private static final String REDUCE_QUANTITY_ENDPOINT = "/api/v1/products/reduce-quantity/";
    private static final String PRODUCTS_ENDPOINT = "/api/v1/products/";

    private final CommendeRepository commendeRepository;
    private final CommandeProduitRepository commandeProduitRepository;
    private final RestTemplate restTemplate;
    private final ProduitServiceClient produitServiceClient;
    private final MouvementStockServiceClient mouvementServiceClient;

    @Value("${mouvements.service.url}")
    private String mouvementsServiceUrl;
    @Value("${produits.service.url}")
    private String produitsServiceUrl;

    @Override
    public Page<CommandeResponseDTO> findAll(Pageable pageable) {
        return commendeRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public CommandeResponseDTO creerCommende(CommandeRequestDTO commandeRequest) {
        Commande commande = new Commande()
                .idFournisseur(commandeRequest.idFournisseur())
                .status(CommendeStatus.EN_ATTENTE);

        Commande savedCommande = commendeRepository.save(commande);

        commandeRequest.produits().stream()
                .map(cmdProduit -> createCommandeProduit(cmdProduit, savedCommande.id()))
                .filter(Objects::nonNull)
                .forEach(commandeProduitRepository::save);

        return convertToResponseDTO(savedCommande);
    }

    @Override
    public CommandeResponseDTO findById(Long id) {
        return commendeRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new CommandeNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(id);
        commandeProduitRepository.deleteAll(commandeProduits);
        commendeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommandeResponseDTO updateCommendeStatus(Long id, CommendeStatus status) {
        Commande commande = commendeRepository.findById(id)
                .orElseThrow(() -> new CommandeNotFoundException(id));

        commande.status(status);

        if (status == CommendeStatus.LIVREE) {
            handleOrderDelivery(id);
        }

        return convertToResponseDTO(commendeRepository.save(commande));
    }

    /**
     * Handles the delivery process for an order, including:
     * - Reducing product quantities
     * - Recording stock movements
     */
    private void handleOrderDelivery(Long commandeId) {
        List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(commandeId);

        if (commandeProduits.isEmpty()) {
            log.warn("Aucun produit trouvé pour la commande ID: {}", commandeId);
            return;
        }

        commandeProduits.stream()
                .forEach(cmdProduit -> processProductDelivery(cmdProduit, commandeId));
    }

    /**
     * Processes delivery for a single product in an order
     */
    private void processProductDelivery(CommandeProduit commandeProduit, Long commandeId) {
        Long produitId = commandeProduit.produitId();
        Integer quantite = commandeProduit.quantite();

        log.info("Traitement du produit dans la commande: ID={}, Quantité={}", produitId, quantite);

        try {
            reduceProductQuantity(produitId, quantite);
            recordStockMovement(commandeProduit, commandeId);
        } catch (RestClientException e) {
            log.error(REDUCTION_ERROR_MSG, produitId, e);
            throw new RuntimeException(
                    "Impossible de réduire la quantité du produit ID " + produitId,
                    e
            );
        }
    }

    /**
     * Reduces the stock quantity for a product
     */
    private void reduceProductQuantity(Long produitId, Integer quantite) throws RestClientException {
        restTemplate.patchForObject(
                produitsServiceUrl + REDUCE_QUANTITY_ENDPOINT + produitId,
                Map.of("quantityToReduce", quantite),
                Object.class
        );
        log.info(REDUCTION_SUCCESS_MSG, produitId);
    }

    /**
     * Records a stock movement for the delivered products
     */
    private void recordStockMovement(CommandeProduit commandeProduit, Long commandeId) {
        try {
            mouvementServiceClient.recordMovement(
                    commandeProduit.produitId(),
                    SORTIE_MOVEMENT_TYPE,
                    commandeProduit.quantite(),
                    commandeProduit.unitPrice(),
                    commandeId
            );
            log.info(MOVEMENT_SUCCESS_MSG, commandeProduit.produitId());
        } catch (RestClientException e) {
            log.warn(MOVEMENT_SERVICE_ERROR_MSG, e.getMessage());
        }
    }

    /**
     * Converts a Commande entity to its DTO representation
     */
    private CommandeResponseDTO convertToResponseDTO(Commande commande) {
        List<ProduitDetailDTO> produitDetails = commandeProduitRepository.findByCommandeId(commande.id()).stream()
                .map(this::mapToProduitDetail)
                .filter(Objects::nonNull)
                .toList();

        return new CommandeResponseDTO(
                commande.id(),
                commande.status(),
                commande.idFournisseur(),
                produitDetails
        );
    }

    /**
     * Maps a CommandeProduit to ProduitDetailDTO by fetching product information
     */
    private ProduitDetailDTO mapToProduitDetail(CommandeProduit commandeProduit) {
        return produitServiceClient.getProduct(commandeProduit.produitId())
                .map(produit -> new ProduitDetailDTO(
                        produit.id(),
                        produit.nom(),
                        produit.prix(),
                        produit.quantite(),
                        commandeProduit.unitPrice()
                ))
                .orElse(null);
    }

    /**
     * Creates a function that transforms ProduitCommandeDTO to CommandeProduit entity
     */
    private CommandeProduit createCommandeProduit(ProduitCommandeDTO produitCmd, Long commandeId) {
        try {
            return produitServiceClient.getProduct(produitCmd.produitId())
                    .map(produit -> new CommandeProduit()
                            .commandeId(commandeId)
                            .produitId(produitCmd.produitId())
                            .quantite(produitCmd.quantite())
                            .unitPrice(produit.prix())
                    )
                    .orElse(null);
        } catch (Exception e) {
            log.error(PRODUIT_NOT_FOUND_MSG, produitCmd.produitId(), e);
            return null;
        }
    }
}
