package org.ismail.gestiondescommmendsfournisseurspringboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ismail.gestiondescommmendsfournisseurspringboot.Enum.CommendeStatus;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommendeStatus status;

    private Long idFournisseur;
}
