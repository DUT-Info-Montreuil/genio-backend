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

package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurDTOTest {

    @Test
    void testGettersAndSetters() {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setEmail("jdupont@example.com");
        dto.setMotDePasse("password123");

        assertEquals("Dupont", dto.getNom());
        assertEquals("Jean", dto.getPrenom());
        assertEquals("jdupont@example.com", dto.getEmail());
        assertEquals("password123", dto.getMotDePasse());
    }

    @Test
    void testToStringContainsAllFields() {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        utilisateur.setNom("Durand");
        utilisateur.setPrenom("Alice");
        utilisateur.setEmail("alice.durand@example.com");
        utilisateur.setMotDePasse("pwd456");

        String toString = utilisateur.toString();

        assertTrue(toString.contains("Durand"));
        assertTrue(toString.contains("Alice"));
        assertTrue(toString.contains("alice.durand@example.com"));
        assertTrue(toString.contains("pwd456"));
    }

    @Test
    void testNullFieldsByDefault() {
        UtilisateurDTO utilisateur = new UtilisateurDTO();

        assertNull(utilisateur.getNom());
        assertNull(utilisateur.getPrenom());
        assertNull(utilisateur.getEmail());
        assertNull(utilisateur.getMotDePasse());
    }

    @Test
    void testNotNullFields() {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        utilisateur.setNom("Martin");
        utilisateur.setPrenom("Sophie");
        utilisateur.setEmail("sophie.martin@example.com");
        utilisateur.setMotDePasse("pass789");

        assertNotNull(utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertNotNull(utilisateur.getEmail());
        assertNotNull(utilisateur.getMotDePasse());
    }

    @Test
    void testFieldEquality() {
        UtilisateurDTO dto1 = new UtilisateurDTO();
        dto1.setNom("Toto");
        dto1.setPrenom("Tata");
        dto1.setEmail("test@test.com");
        dto1.setMotDePasse("pass");

        UtilisateurDTO dto2 = new UtilisateurDTO();
        dto2.setNom("Toto");
        dto2.setPrenom("Tata");
        dto2.setEmail("test@test.com");
        dto2.setMotDePasse("pass");

        assertEquals(dto1.getNom(), dto2.getNom());
        assertEquals(dto1.getPrenom(), dto2.getPrenom());
        assertEquals(dto1.getEmail(), dto2.getEmail());
        assertEquals(dto1.getMotDePasse(), dto2.getMotDePasse());
    }
    @Test
    void testEqualsAndHashCode() {
        UtilisateurUpdateDTO dto1 = new UtilisateurUpdateDTO();
        dto1.setNom("Dupont");
        dto1.setPrenom("Alice");
        dto1.setRole("USER");
        dto1.setActif(true);

        UtilisateurUpdateDTO dto2 = new UtilisateurUpdateDTO();
        dto2.setNom("Dupont");
        dto2.setPrenom("Alice");
        dto2.setRole("USER");
        dto2.setActif(true);

        UtilisateurUpdateDTO dto3 = new UtilisateurUpdateDTO();
        dto3.setNom("Martin");
        dto3.setPrenom("Paul");
        dto3.setRole("ADMIN");
        dto3.setActif(false);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}