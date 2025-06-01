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

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void testBuilderAndGetters() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();

        Utilisateur u = Utilisateur.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jdupont@gmail.com")
                .motDePasse("mdp")
                .createdAt(created)
                .updatedAt(updated)
                .role("ADMIN")
                .actif(true)
                .build();

        assertEquals(1L, u.getId());
        assertEquals("Dupont", u.getNom());
        assertEquals("Jean", u.getPrenom());
        assertEquals("jdupont@gmail.com", u.getEmail());
        assertEquals("mdp", u.getMotDePasse());
        assertEquals(created, u.getCreatedAt());
        assertEquals(updated, u.getUpdatedAt());
        assertEquals("ADMIN", u.getRole());
        assertTrue(u.isActif());
    }

    @Test
    void testDefaultValues() {
        Utilisateur u = Utilisateur.builder()
                .nom("Test")
                .prenom("User")
                .email("test@user.com")
                .motDePasse("pass")
                .build();

        assertEquals("NONE", u.getRole());
        assertTrue(u.isActif());
    }

    @Test
    void testSetters() {
        Utilisateur u = new Utilisateur();
        u.setId(42L);
        u.setNom("Alice");
        u.setPrenom("Martin");
        u.setEmail("alice@example.com");
        u.setMotDePasse("securePass");
        u.setRole("MANAGER");
        u.setActif(false);
        LocalDateTime now = LocalDateTime.now();
        u.setCreatedAt(now);
        u.setUpdatedAt(now);

        assertEquals(42L, u.getId());
        assertEquals("Alice", u.getNom());
        assertEquals("Martin", u.getPrenom());
        assertEquals("alice@example.com", u.getEmail());
        assertEquals("securePass", u.getMotDePasse());
        assertEquals("MANAGER", u.getRole());
        assertFalse(u.isActif());
        assertEquals(now, u.getCreatedAt());
        assertEquals(now, u.getUpdatedAt());
    }

    @Test
    void testToString_containsAllFields() {
        Utilisateur u = Utilisateur.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .email("john.doe@example.com")
                .motDePasse("secret")
                .role("ADMIN")
                .actif(true)
                .build();

        String str = u.toString();

        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("John"));
        assertTrue(str.contains("john.doe@example.com"));
        assertTrue(str.contains("ADMIN"));
        assertTrue(str.contains("true"));
    }
}