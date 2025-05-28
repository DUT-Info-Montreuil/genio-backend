/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

package com.genio.model;

import com.genio.repository.MaitreDeStageRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

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

    static Stream<MaitreDeStage> invalidMaitreDeStageProvider() {
        return Stream.of(
                newMaitre(" ", "Claire", "claire@example.com"),
                newMaitre("Martin", "", "claire@example.com"),
                newMaitre("Martin", "Claire", " "),
                newMaitre("Martin", "Claire", "not-an-email")
        );
    }

    private static MaitreDeStage newMaitre(String nom, String prenom, String email) {
        MaitreDeStage mds = new MaitreDeStage();
        mds.setNom(nom);
        mds.setPrenom(prenom);
        mds.setEmail(email);
        return mds;
    }

    @ParameterizedTest
    @MethodSource("invalidMaitreDeStageProvider")
    @DisplayName("Échec de validation pour un maître de stage invalide")
    void shouldFailForInvalidFields(MaitreDeStage invalidMds) {
        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(invalidMds));
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