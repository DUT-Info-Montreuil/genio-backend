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