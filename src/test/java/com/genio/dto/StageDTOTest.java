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

class StageDTOTest {

    @Test
    void testConstructorAndGetters() {
        StageDTO dto = StageDTO.builder()
                .anneeStage("2025")
                .sujetDuStage("Développement d'une appli")
                .dateDebutStage("2025-06-01")
                .dateFinStage("2025-08-31")
                .duree("3 mois")
                .joursTot(66)
                .heuresTot(462)
                .remunerationHoraire("4.5€")
                .saeStageProfessionnel("Stage SAE")
                .build();

        assertEquals("2025", dto.getAnneeStage());
        assertEquals("Développement d'une appli", dto.getSujetDuStage());
        assertEquals("2025-06-01", dto.getDateDebutStage());
        assertEquals("2025-08-31", dto.getDateFinStage());
        assertEquals("3 mois", dto.getDuree());
        assertEquals(66, dto.getJoursTot());
        assertEquals(462, dto.getHeuresTot());
        assertEquals("4.5€", dto.getRemunerationHoraire());
        assertEquals("Stage SAE", dto.getSaeStageProfessionnel());
    }

    @Test
    void testSetterAnneeStage() {
        StageDTO dto = StageDTO.builder()
                .anneeStage("2025")
                .sujetDuStage("sujet")
                .dateDebutStage("2025-06-01")
                .dateFinStage("2025-08-31")
                .duree("3 mois")
                .joursTot(60)
                .heuresTot(400)
                .remunerationHoraire("5.0€")
                .saeStageProfessionnel("SAE")
                .build();
        dto.setAnneeStage("2026");
        assertEquals("2026", dto.getAnneeStage());
    }

    @Test
    void testToString() {
        StageDTO dto = StageDTO.builder()
                .anneeStage("2024")
                .sujetDuStage("Sujet de test")
                .dateDebutStage("2024-06-01")
                .dateFinStage("2024-08-31")
                .duree("2 mois")
                .joursTot(40)
                .heuresTot(300)
                .remunerationHoraire("3.0€")
                .saeStageProfessionnel("SAE2")
                .build();
        String result = dto.toString();
        assertTrue(result.contains("Sujet de test"));
        assertTrue(result.contains("2024-06-01"));
        assertTrue(result.contains("2024-08-31"));
        assertTrue(result.contains("2 mois"));
        assertTrue(result.contains("3.0€"));
        assertTrue(result.contains("SAE2"));
        assertTrue(result.contains("2024"));
    }


}