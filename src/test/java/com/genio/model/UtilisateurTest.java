package com.genio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void testBuilderAndGetters() {
        Utilisateur u = Utilisateur.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .username("jdupont")
                .motDePasse("mdp")
                .role("ADMIN")
                .actif(true)
                .build();

        assertEquals(1L, u.getId());
        assertEquals("Dupont", u.getNom());
        assertEquals("Jean", u.getPrenom());
        assertEquals("jdupont", u.getUsername());
        assertEquals("mdp", u.getMotDePasse());
        assertEquals("ADMIN", u.getRole());
        assertTrue(u.isActif());
    }

    @Test
    void testSetters() {
        Utilisateur u = new Utilisateur();
        u.setNom("Toto");
        assertEquals("Toto", u.getNom());
    }
}