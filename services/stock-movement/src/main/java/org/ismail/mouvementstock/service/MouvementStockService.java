package org.ismail.mouvementstock.service;

import org.ismail.mouvementstock.dto.MouvementStockRequestDTO;
import org.ismail.mouvementstock.dto.MouvementStockResponseDTO;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MouvementStockService {

    MouvementStockResponseDTO createMouvement(MouvementStockRequestDTO requestDTO);

    Page<MouvementStockResponseDTO> getAllMouvements(Pageable pageable);

    List<MouvementStockResponseDTO> getMouvementsByProduitId(Long produitId);

    List<MouvementStockResponseDTO> getMouvementsByType(TypeMouvement type);

    List<MouvementStockResponseDTO> getMouvementsByCommande(Long refCommande);

    void deleteMouvement(Long id);
}
