package com.genio.model;

import com.genio.repository.MaitreDeStageRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MaitreDeStageTest {

    @Autowired
    private MaitreDeStageRepository repository;

    @Test
    @DisplayName("Insertion réussie d'un maître de stage valide")
    void shouldSaveValidMaitreDeStage() {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom("Martin");
        mds.setPrenom("Claire");
        mds.setEmail("claire.martin@example.com");

        MaitreDeStage saved = repository.save(mds);

        assertNotNull(saved.getId());
        assertEquals("Martin", saved.getNom());
        assertEquals("Claire", saved.getPrenom());
        assertEquals("claire.martin@example.com", saved.getEmail());
    }

    @Test
    @DisplayName("Échec si le nom est vide")
    void shouldFailWhenNomIsBlank() {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom(" ");
        mds.setPrenom("Claire");
        mds.setEmail("claire.martin@example.com");

        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(mds));
    }

    @Test
    @DisplayName("Échec si le prénom est vide")
    void shouldFailWhenPrenomIsBlank() {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom("Martin");
        mds.setPrenom("");
        mds.setEmail("claire.martin@example.com");

        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(mds));
    }

    @Test
    @DisplayName("Échec si l'email est vide")
    void shouldFailWhenEmailIsBlank() {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom("Martin");
        mds.setPrenom("Claire");
        mds.setEmail(" ");

        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(mds));
    }

    @Test
    @DisplayName("Échec si l'email est invalide")
    void shouldFailWhenEmailIsInvalid() {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom("Martin");
        mds.setPrenom("Claire");
        mds.setEmail("not-an-email");

        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(mds));
    }

    @Test
    void testSettersAndGetters() {
        MaitreDeStage mds = new MaitreDeStage();

        mds.setId(5L);
        mds.setNom("Martin");
        mds.setPrenom("Luc");
        mds.setEmail("luc.martin@entreprise.com");

        assertEquals(5L, mds.getId());
        assertEquals("Martin", mds.getNom());
        assertEquals("Luc", mds.getPrenom());
        assertEquals("luc.martin@entreprise.com", mds.getEmail());
    }
}