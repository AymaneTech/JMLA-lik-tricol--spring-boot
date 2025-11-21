package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    private CommendeStatus status;
}
