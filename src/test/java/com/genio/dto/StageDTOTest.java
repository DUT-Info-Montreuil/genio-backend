package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StageDTOTest {

    @Test
    void testConstructorAndGetters() {
        StageDTO dto = new StageDTO(
                "2025",
                "Développement d'une appli",
                "2025-06-01",
                "2025-08-31",
                "3 mois",
                66,
                462,
                "4.5€",
                "Stage SAE"
        );

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
        StageDTO dto = new StageDTO(
                "2025", "sujet", "2025-06-01", "2025-08-31", "3 mois",
                60, 400, "5.0€", "SAE"
        );
        dto.setAnneeStage("2026");
        assertEquals("2026", dto.getAnneeStage());
    }

    @Test
    void testToString() {
        StageDTO dto = new StageDTO(
                "2024", "Sujet de test", "2024-06-01", "2024-08-31", "2 mois",
                40, 300, "3.0€", "SAE2"
        );
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