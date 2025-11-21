package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "commande_produit")
@Getter
@Setter
@Accessors(chain = true, fluent = true)
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
