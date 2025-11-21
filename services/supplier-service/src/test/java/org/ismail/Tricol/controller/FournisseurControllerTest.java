package org.ismail.Tricol.controller;

import org.ismail.Tricol.config.TestConfig;
import org.ismail.Tricol.repository.FournisseurRepository;
import org.ismail.Tricol.service.FournisseurServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
class FournisseurControllerTest {

    @Autowired
    private FournisseurRepository fournisseurRepository;


    @Test
    void save() {
        // given
        FournisseurServiceImp fournisseurServiceImp = new FournisseurServiceImp();
        fournisseurServiceImp.setFournisseurRepository(fournisseurRepository);
        FournisseurController fournisseurController = new FournisseurController();
        fournisseurController.setFournisseurService(fournisseurServiceImp);
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllOrderByNomAsc() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByNom() {
    }

    @Test
    void deleteFournisseur() {
    }

    @Test
    void updateFournisseur() {
    }
}