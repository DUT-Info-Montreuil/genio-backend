package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TuteurDTOTest {

    @Test
    void testConstructorAndGetters() {
        TuteurDTO tuteur = new TuteurDTO("Dupont", "Jean", "jean.dupont@example.com");

        assertEquals("Dupont", tuteur.getNom());
        assertEquals("Jean", tuteur.getPrenom());
        assertEquals("jean.dupont@example.com", tuteur.getEmail());
    }

    @Test
    void testSetNom() {
        TuteurDTO tuteur = new TuteurDTO("X", "Y", "z@example.com");
        tuteur.setNom("Durand");
        assertEquals("Durand", tuteur.getNom());
    }

    @Test
    void testToString() {
        TuteurDTO tuteur = new TuteurDTO("Nom", "Prénom", "email@example.com");
        String result = tuteur.toString();
        assertTrue(result.contains("Nom"));
        assertTrue(result.contains("Prénom"));
        assertTrue(result.contains("email@example.com"));
    }
}