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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.model;

import com.genio.repository.EtudiantRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EtudianTest {

    @Autowired
    private EtudiantRepository repository;

    @Test
    @DisplayName("Insertion réussie d'un étudiant valide")
    void shouldSaveValidEtudiant() {
        Etudiant e = new Etudiant();
        e.setNom("Dupont");
        e.setPrenom("Marie");
        e.setEmail("marie.dupont@email.com");
        e.setPromotion("BUT3");

        Etudiant saved = repository.save(e);
        assertNotNull(saved.getId());
        assertEquals("Dupont", saved.getNom());
        assertEquals("Marie", saved.getPrenom());
        assertEquals("marie.dupont@email.com", saved.getEmail());
        assertEquals("BUT3", saved.getPromotion());
    }

    @Test
    @DisplayName("Échec si le nom est vide")
    void shouldFailWhenNomIsBlank() {
        Etudiant e = new Etudiant();
        e.setNom("");
        e.setPrenom("Marie");
        e.setEmail("test@test.com");
        e.setPromotion("BUT2");

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(e);
        });
    }

    @Test
    @DisplayName("Échec si le prénom est vide")
    void shouldFailWhenPrenomIsBlank() {
        Etudiant e = new Etudiant();
        e.setNom("Nom");
        e.setPrenom(" ");
        e.setEmail("test@test.com");
        e.setPromotion("BUT2");

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(e);
        });
    }

    @Test
    @DisplayName("Échec si l'email est vide")
    void shouldFailWhenEmailIsBlank() {
        Etudiant e = new Etudiant();
        e.setNom("Nom");
        e.setPrenom("Prenom");
        e.setEmail(" ");
        e.setPromotion("BUT2");

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(e);
        });
    }

    @Test
    @DisplayName("Échec si l'email est invalide")
    void shouldFailWhenEmailIsInvalid() {
        Etudiant e = new Etudiant();
        e.setNom("Nom");
        e.setPrenom("Prenom");
        e.setEmail("invalid-email");
        e.setPromotion("BUT2");

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(e);
        });
    }

    @Test
    @DisplayName("Échec si la promotion est vide")
    void shouldFailWhenPromotionIsBlank() {
        Etudiant e = new Etudiant();
        e.setNom("Nom");
        e.setPrenom("Prenom");
        e.setEmail("email@test.com");
        e.setPromotion(" ");

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(e);
        });
    }
    @Test
    void testSettersAndGetters() {
        Etudiant etudiant = new Etudiant();

        etudiant.setId(1L);
        etudiant.setNom("Dupont");
        etudiant.setPrenom("Alice");
        etudiant.setEmail("alice.dupont@example.com");
        etudiant.setPromotion("BUT3");

        assertEquals(1L, etudiant.getId());
        assertEquals("Dupont", etudiant.getNom());
        assertEquals("Alice", etudiant.getPrenom());
        assertEquals("alice.dupont@example.com", etudiant.getEmail());
        assertEquals("BUT3", etudiant.getPromotion());
    }

}