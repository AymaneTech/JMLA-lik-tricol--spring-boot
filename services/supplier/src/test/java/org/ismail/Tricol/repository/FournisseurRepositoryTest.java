package org.ismail.Tricol.repository;

import org.ismail.Tricol.config.TestConfig;
import org.ismail.Tricol.model.Fournisseur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
class FournisseurRepositoryTest {

    @Autowired
    private FournisseurRepository underTest;

    @Test
    void itShouldfindByNom() {
        // given
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom("Test Fournisseur");
        fournisseur.setPrenom("Prenom Test");
        fournisseur.setEmail("test@example.com");
        fournisseur.setSociete("Societe Test");
        fournisseur.setAdresse("123 Rue Test");
        fournisseur.setContact("Contact Test");
        fournisseur.setTelephone("0612345678");
        fournisseur.setVille("Casablanca");
        fournisseur.setIce("ICE123456");
        underTest.save(fournisseur);
        underTest.flush();

        // when
        Fournisseur result = underTest.findByNom("Test Fournisseur");

        // then
        assertNotNull(result);
        assertEquals("Test Fournisseur", result.getNom());
    }

    @Test
    void itShouldfindAllOrderByNomAsc() {
        // given
        Fournisseur f1 = new Fournisseur();
        f1.setNom("Zebra");
        f1.setPrenom("Prenom1");
        f1.setEmail("zebra@example.com");
        f1.setSociete("Societe Zebra");
        f1.setAdresse("123 Rue Zebra");
        f1.setContact("Contact Zebra");
        f1.setTelephone("0612345678");
        f1.setVille("Rabat");
        f1.setIce("ICE111111");

        Fournisseur f2 = new Fournisseur();
        f2.setNom("Alpha");
        f2.setPrenom("Prenom2");
        f2.setEmail("alpha@example.com");
        f2.setSociete("Societe Alpha");
        f2.setAdresse("456 Rue Alpha");
        f2.setContact("Contact Alpha");
        f2.setTelephone("0687654321");
        f2.setVille("Marrakech");
        f2.setIce("ICE222222");

        underTest.save(f1);
        underTest.save(f2);
        underTest.flush();

        // when
        var results = underTest.findAllOrderByNomAsc();

        // then
        assertFalse(results.isEmpty());
        assertEquals("Alpha", results.get(0).getNom());
    }
}
