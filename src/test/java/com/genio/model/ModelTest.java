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

class ModelTest {
    @Test
    void testModeleFields() {
        Modele modele = new Modele();
        modele.setId(1L);
        modele.setTitre("Convention 2025");
        modele.setDescriptionModification("Ajout de clauses RGPD");
        modele.setArchivedAt(LocalDateTime.of(2024, 12, 31, 10, 0));
        modele.setArchived(true);
        modele.setDateDerniereModification(LocalDateTime.of(2025, 1, 15, 14, 30));
        modele.setNom("Stage BUT3");
        modele.setAnnee("2025");
        modele.setFichierBinaire("01010101".getBytes());
        modele.setFichierHash("abcdef1234567890");

        assertEquals(1L, modele.getId());
        assertEquals("Convention 2025", modele.getTitre());
        assertEquals("Ajout de clauses RGPD", modele.getDescriptionModification());
        assertEquals(LocalDateTime.of(2024, 12, 31, 10, 0), modele.getArchivedAt());
        assertTrue(modele.isArchived());
        assertEquals(LocalDateTime.of(2025, 1, 15, 14, 30), modele.getDateDerniereModification());
        assertEquals("Stage BUT3", modele.getNom());
        assertEquals("2025", modele.getAnnee());
        assertArrayEquals("01010101".getBytes(), modele.getFichierBinaire());
        assertEquals("abcdef1234567890", modele.getFichierHash());
    }
}
