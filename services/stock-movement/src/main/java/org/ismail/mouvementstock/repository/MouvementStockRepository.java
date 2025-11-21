package org.ismail.mouvementstock.repository;

import org.ismail.mouvementstock.model.MouvementStock;
import org.ismail.mouvementstock.model.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    List<MouvementStock> findByProduitId(Long produitId);

    List<MouvementStock> findByTypeMvt(TypeMouvement typeMvt);

    List<MouvementStock> findByRefCommande(Long refCommande);
}

