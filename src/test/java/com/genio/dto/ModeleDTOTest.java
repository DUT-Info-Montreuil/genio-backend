package com.genio.dto;

import com.genio.dto.outputmodeles.ModeleDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModeleDTOTest {

    @Test
    void testConstructorAndGetters() {
        ModeleDTO dto = new ModeleDTO(
                1L,
                "modele2025.docx",
                "2025",
                "docx",
                "2024-01-01",
                "Titre test",
                "Description test"
        );

        assertEquals(1L, dto.getId());
        assertEquals("modele2025.docx", dto.getNom());
        assertEquals("2025", dto.getAnnee());
        assertEquals("docx", dto.getFormat());
        assertEquals("2024-01-01", dto.getDateCreation());
        assertEquals("Titre test", dto.getTitre());
        assertEquals("Description test", dto.getDescriptionModification());
    }

    @Test
    void testSetters() {
        ModeleDTO dto = new ModeleDTO(
                null, null, null, null, null, null, null
        );

        dto.setId(2L);
        dto.setNom("modele2026.docx");
        dto.setAnnee("2026");
        dto.setFormat("pdf");
        dto.setDateCreation("2024-04-08");
        dto.setTitre("Titre changé");
        dto.setDescriptionModification("Modifié manuellement");

        assertEquals(2L, dto.getId());
        assertEquals("modele2026.docx", dto.getNom());
        assertEquals("2026", dto.getAnnee());
        assertEquals("pdf", dto.getFormat());
        assertEquals("2024-04-08", dto.getDateCreation());
        assertEquals("Titre changé", dto.getTitre());
        assertEquals("Modifié manuellement", dto.getDescriptionModification());
    }

    @Test
    void testToString() {
        ModeleDTO dto = new ModeleDTO(
                3L,
                "model.docx",
                "2023",
                "docx",
                "2023-05-10",
                "Titre de test",
                "Desc test"
        );

        String result = dto.toString();
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("nom='model.docx'"));
        assertTrue(result.contains("annee='2023'"));
        assertTrue(result.contains("format='docx'"));
        assertTrue(result.contains("dateCreation='2023-05-10'"));
        assertTrue(result.contains("titre='Titre de test'")); // optionnel selon le `toString`
    }
}