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

import com.genio.dto.outputmodeles.ModeleDTOForList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModeleDTOForListTest {

    @Test
    void testConstructorAndGetters() {
        ModeleDTOForList dto = new ModeleDTOForList(
                1L,
                "modele2025.docx",
                "Description du modèle",
                "docx",
                "Titre 2025"
        );

        assertEquals(1L, dto.getId());
        assertEquals("modele2025.docx", dto.getNom());
        assertEquals("Description du modèle", dto.getDescription());
        assertEquals("docx", dto.getFormat());
        assertEquals("Titre 2025", dto.getTitre());
    }

    @Test
    void testSetters() {
        ModeleDTOForList dto = new ModeleDTOForList(
                null, null, null, null, null
        );

        dto.setId(2L);
        dto.setNom("modele2026.docx");
        dto.setDescription("Modèle pour l'année 2026");
        dto.setFormat("pdf");
        dto.setTitre("Titre 2026");
        dto.setDateDerniereModification("2024-04-15");

        assertEquals(2L, dto.getId());
        assertEquals("modele2026.docx", dto.getNom());
        assertEquals("Modèle pour l'année 2026", dto.getDescription());
        assertEquals("pdf", dto.getFormat());
        assertEquals("Titre 2026", dto.getTitre());
        assertEquals("2024-04-15", dto.getDateDerniereModification());
    }

    @Test
    void testToString() {
        ModeleDTOForList dto = new ModeleDTOForList(
                3L,
                "modele2027.docx",
                "Modèle 2027",
                "docx",
                "Titre 2027"
        );

        String result = dto.toString();
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("nom='modele2027.docx'"));
        assertTrue(result.contains("description='Modèle 2027'"));
        assertTrue(result.contains("format='docx'"));
        assertTrue(result.contains("titre='Titre 2027'"));
    }
}