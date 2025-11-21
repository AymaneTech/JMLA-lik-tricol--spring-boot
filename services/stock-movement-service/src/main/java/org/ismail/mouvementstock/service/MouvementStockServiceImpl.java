package org.ismail.mouvementstock.service;

import org.ismail.mouvementstock.client.ProductClient;
import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.dto.ProductDTO;
import org.ismail.mouvementstock.exception.ResourceNotFoundException;
import org.ismail.mouvementstock.model.MouvementStock;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.ismail.mouvementstock.repository.MouvementStockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository repository;
    private final ProductClient productClient;

    public MouvementStockServiceImpl(MouvementStockRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    @Override
    public MouvementStockResponseDTO createMouvement(MouvementStockRequestDTO requestDTO) {
        // Vérifier que le produit existe
        ProductDTO product = productClient.getProductById(requestDTO.getProduitId());
        if (product == null) {
            throw new ResourceNotFoundException("Produit non trouvé avec l'ID: " + requestDTO.getProduitId());
        }

        // Valider la quantité selon le type
        validateQuantity(requestDTO, product);

        // Créer l'entité
        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduitId(requestDTO.getProduitId());
        mouvement.setTypeMvt(requestDTO.getTypeMvt());
        mouvement.setQuantite(requestDTO.getQuantite());
        mouvement.setPrixAchat(requestDTO.getPrixAchat());
        mouvement.setDateMvt(LocalDateTime.now());
        mouvement.setRefCommande(requestDTO.getRefCommande());

        // Sauvegarder
        mouvement = repository.save(mouvement);

        // Calculer et mettre à jour le stock
        Integer newQuantity = calculateNewStock(product.getQuantiteStock(), requestDTO);
        productClient.updateProductQuantity(product.getId(), newQuantity);

        return convertToResponseDTO(mouvement);
    }

    @Override
    public Page<MouvementStockResponseDTO> getAllMouvements(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByProduitId(Long produitId) {
        return repository.findByProduitId(produitId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByType(TypeMouvement type) {
        return repository.findByTypeMvt(type).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByCommande(Long refCommande) {
        return repository.findByRefCommande(refCommande).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMouvement(Long id) {
        MouvementStock mouvement = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mouvement non trouvé avec l'ID: " + id));

        // Récupérer le produit actuel
        ProductDTO product = productClient.getProductById(mouvement.getProduitId());

        // Restaurer le stock
        Integer restoredQuantity = restoreStock(product.getQuantiteStock(), mouvement);
        productClient.updateProductQuantity(product.getId(), restoredQuantity);

        // Supprimer le mouvement
        repository.deleteById(id);
    }

    private void validateQuantity(MouvementStockRequestDTO requestDTO, ProductDTO product) {
        if (requestDTO.getQuantite() == null || requestDTO.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        if (requestDTO.getTypeMvt() == TypeMouvement.SORTIE) {
            if (requestDTO.getQuantite() > product.getQuantiteStock()) {
                throw new IllegalArgumentException("Stock insuffisant. Stock actuel: " + product.getQuantiteStock());
            }
        }
    }

    private Integer calculateNewStock(Integer currentStock, MouvementStockRequestDTO requestDTO) {
        return switch (requestDTO.getTypeMvt()) {
            case ENTREE -> currentStock + requestDTO.getQuantite();
            case SORTIE -> currentStock - requestDTO.getQuantite();
            case AJUSTEMENT -> requestDTO.getQuantite();
        };
    }

    private Integer restoreStock(Integer currentStock, MouvementStock mouvement) {
        return switch (mouvement.getTypeMvt()) {
            case ENTREE -> currentStock - mouvement.getQuantite();
            case SORTIE -> currentStock + mouvement.getQuantite();
            case AJUSTEMENT -> throw new IllegalStateException("Impossible de supprimer un ajustement de stock");
        };
    }

    private MouvementStockResponseDTO convertToResponseDTO(MouvementStock mouvement) {
        MouvementStockResponseDTO dto = new MouvementStockResponseDTO();
        dto.setId(mouvement.getId());
        dto.setProduitId(mouvement.getProduitId());
        dto.setTypeMvt(mouvement.getTypeMvt());
        dto.setQuantite(mouvement.getQuantite());
        dto.setPrixAchat(mouvement.getPrixAchat());
        dto.setDateMvt(mouvement.getDateMvt());
        dto.setRefCommande(mouvement.getRefCommande());
        return dto;
    }
}
