package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeRequestDTO;
import org.ismail.gestiondescommmendsfournisseurspringboot.dto.CommandeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommendeService {

     Page<CommandeResponseDTO> findAll(Pageable pageable);
     CommandeResponseDTO creerCommende(CommandeRequestDTO commandeRequest);
     CommandeResponseDTO findById(Long id);
     void deleteById(Long id);
     CommandeResponseDTO updateCommendeStatus(Long id, CommendeStatus status);
}
