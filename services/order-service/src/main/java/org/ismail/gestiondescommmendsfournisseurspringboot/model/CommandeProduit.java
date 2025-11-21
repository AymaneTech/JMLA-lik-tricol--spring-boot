package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "commande_produit")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandeProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long commandeId;
    private Long produitId;
    private Integer quantite;
    private Double unitPrice;

}
