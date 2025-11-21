package org.ismail.mouvementstock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long produitId;  // Référence au Product Service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement typeMvt;

    @Column(nullable = false)
    private Integer quantite;

    @Column
    private Double prixAchat;

    @Column(nullable = false)
    private LocalDateTime dateMvt;

    @Column
    private Long refCommande;
}

