package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import org.ismail.gestiondescommmendsfournisseurspringboot.enums.CommendeStatus;

public record UpdateStatusRequest(CommendeStatus status) {
}
