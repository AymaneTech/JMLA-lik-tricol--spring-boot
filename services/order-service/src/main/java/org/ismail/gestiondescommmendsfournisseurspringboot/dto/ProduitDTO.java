package org.ismail.gestiondescommmendsfournisseurspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {
    public Long id;
    public String nom;
    public Double prix;
    public Integer quantite;
}
