package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.enums.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeRequestDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommendeService {

     Page<CommandeResponseDTO> findAll(Pageable pageable);
     CommandeResponseDTO creerCommende(CommandeRequestDTO commandeRequest);
     CommandeResponseDTO findById(Long id);
     void deleteById(Long id);
     CommandeResponseDTO updateCommendeStatus(Long id, CommendeStatus status);
}
