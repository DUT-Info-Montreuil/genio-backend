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