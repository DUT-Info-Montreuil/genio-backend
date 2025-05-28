package com.genio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TuteurTest {

    @Test
    void testSettersAndGetters() {
        Tuteur tuteur = new Tuteur();

        tuteur.setId(1L);
        tuteur.setNom("Martin");
        tuteur.setPrenom("Sophie");
        tuteur.setEmail("sophie.martin@example.com");

        assertEquals(1L, tuteur.getId());
        assertEquals("Martin", tuteur.getNom());
        assertEquals("Sophie", tuteur.getPrenom());
        assertEquals("sophie.martin@example.com", tuteur.getEmail());
    }
}