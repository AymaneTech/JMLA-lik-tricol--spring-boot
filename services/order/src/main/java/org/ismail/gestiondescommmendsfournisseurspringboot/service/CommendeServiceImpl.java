package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.*;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commande;
import org.ismail.gestiondescommmendsfournisseurspringboot.model.CommandeProduit;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommandeProduitRepository;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommendeServiceImpl implements CommendeService {

    @Autowired
    public CommendeRepository commendeRepository;

    @Autowired
    public CommandeProduitRepository commandeProduitRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mouvements.service.url}")
    private String mouvementsServiceUrl;

    @Value("${produits.service.url}")
    private String produitsServiceUrl;

    @Override
    public Page<CommandeResponseDTO> findAll(Pageable pageable) {
        Page<Commande> commandes = commendeRepository.findAll(pageable);
        return commandes.map(this::convertToResponseDTO);
    }

    @Override
    public CommandeResponseDTO creerCommende(CommandeRequestDTO commandeRequest) {
        Commande commande = new Commande();
        commande.setIdFournisseur(commandeRequest.getIdFournisseur());
        commande.setStatus(CommendeStatus.EN_ATTENTE);

        Commande savedCommande = commendeRepository.save(commande);

        for (ProduitCommandeDTO produitCmd : commandeRequest.getProduits()) {
            try {
               ProduitDTO produit = restTemplate.getForObject(
                    produitsServiceUrl + "/api/v1/products/" + produitCmd.getProduitId(),
                    ProduitDTO.class
                );

                if (produit != null) {
                    CommandeProduit commandeProduit = new CommandeProduit();
                    commandeProduit.setCommandeId(savedCommande.getId());
                    commandeProduit.setProduitId(produitCmd.getProduitId());
                    commandeProduit.setQuantite(produitCmd.getQuantite());
                    commandeProduit.setUnitPrice(produit.getPrix());

                    commandeProduitRepository.save(commandeProduit);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du produit ID "
                    + produitCmd.getProduitId() + ": " + e.getMessage());
            }
        }

        return convertToResponseDTO(savedCommande);
    }

    @Override
    public CommandeResponseDTO findById(Long id) {
        Commande commande = commendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id: " + id));
        return convertToResponseDTO(commande);
    }

    @Override
    public void deleteById(Long id) {
        commandeProduitRepository.deleteAll(commandeProduitRepository.findByCommandeId(id));
        // Puis supprimer la commande
        commendeRepository.deleteById(id);
    }

    @Override
    public CommandeResponseDTO updateCommendeStatus(Long id, CommendeStatus status) {
        Commande commande = commendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id: " + id));

        commande.setStatus(status);
        Commande savedCommande = commendeRepository.save(commande);

        if(status == CommendeStatus.LIVREE) {
            List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(id);

            if (commandeProduits.isEmpty()) {
                System.out.println("Aucun produit trouvé pour la commande ID: " + id);
                return convertToResponseDTO(savedCommande);
            }

            for (CommandeProduit commandeProduit : commandeProduits) {
                System.out.println("Produit dans la commande: ID=" + commandeProduit.getProduitId()
                        + ", Quantité=" + commandeProduit.getQuantite());

                // Réduire la quantité du produit
                Map<String, Object> produitData = new HashMap<>();
                produitData.put("quantityToReduce", commandeProduit.getQuantite());

                try {
                    // Préparer les headers pour la requête PATCH
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(produitData, headers);

                    // Appel PATCH pour réduire la quantité du produit
                    restTemplate.exchange(
                            produitsServiceUrl + "/api/v1/products/reduce-quantity/" + commandeProduit.getProduitId(),
                            HttpMethod.PATCH,
                            requestEntity,
                            Object.class
                    );

                    System.out.println("Quantité réduite pour le produit ID: " + commandeProduit.getProduitId());

                    // Créer un mouvement de stock SORTIE (optionnel si le service existe)
                    try {
                        Map<String, Object> mouvementData = new HashMap<>();
                        mouvementData.put("produitId", commandeProduit.getProduitId());
                        mouvementData.put("typeMvt", "SORTIE");
                        mouvementData.put("quantite", commandeProduit.getQuantite());
                        mouvementData.put("prixAchat", commandeProduit.getUnitPrice());
                        mouvementData.put("refCommande", id);

                        restTemplate.postForObject(
                                mouvementsServiceUrl + "/api/v1/mouvements",
                                mouvementData,
                                Object.class
                        );

                        System.out.println("Mouvement de stock SORTIE créé pour le produit ID: " + commandeProduit.getProduitId());
                    } catch (Exception e) {
                        System.err.println("Le service de mouvement n'est pas disponible (optionnel): " + e.getMessage());
                        // On continue même si le service de mouvement n'est pas disponible
                    }

                } catch (Exception e) {
                    System.err.println("Erreur lors de la réduction de quantité pour le produit ID "
                            + commandeProduit.getProduitId() + ": " + e.getMessage());
                    throw new RuntimeException("Impossible de réduire la quantité du produit ID "
                            + commandeProduit.getProduitId() + ": " + e.getMessage());
                }
            }
        }

        return convertToResponseDTO(savedCommande);
    }

    private CommandeResponseDTO convertToResponseDTO(Commande commande) {
        CommandeResponseDTO responseDTO = new CommandeResponseDTO();
        responseDTO.setId(commande.getId());
        responseDTO.setStatus(commande.getStatus());
        responseDTO.setIdFournisseur(commande.getIdFournisseur());

        // Récupérer les produits de la commande
        List<CommandeProduit> commandeProduits = commandeProduitRepository.findByCommandeId(commande.getId());

        List<ProduitDetailDTO> produitDetails = new ArrayList<>();
        for (CommandeProduit cp : commandeProduits) {
            try {
                ProduitDTO produit = restTemplate.getForObject(
                    produitsServiceUrl + "/api/v1/products/" + cp.getProduitId(),
                    ProduitDTO.class
                );

                if (produit != null) {
                    ProduitDetailDTO detail = new ProduitDetailDTO();
                    detail.setId(produit.getId());
                    detail.setNom(produit.getNom());
                    detail.setPrix(produit.getPrix());
                    detail.setQuantite(cp.getQuantite());
                    detail.setUnitPrice(cp.getUnitPrice());
                    produitDetails.add(detail);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du produit ID "
                    + cp.getProduitId() + ": " + e.getMessage());
            }
        }

        responseDTO.setProduits(produitDetails);
        return responseDTO;
    }
}
