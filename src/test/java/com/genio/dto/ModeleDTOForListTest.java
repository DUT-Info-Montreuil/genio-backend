package com.genio.dto;

import com.genio.dto.outputmodeles.ModeleDTOForList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModeleDTOForListTest {

    @Test
    void testConstructorAndGetters() {
        ModeleDTOForList dto = new ModeleDTOForList(1L, "modele2025.docx", "Description du modèle", "docx");

        assertEquals(1L, dto.getId());
        assertEquals("modele2025.docx", dto.getNom());
        assertEquals("Description du modèle", dto.getDescription());
        assertEquals("docx", dto.getFormat());
    }

    @Test
    void testSetters() {
        ModeleDTOForList dto = new ModeleDTOForList(null, null, null, null);

        dto.setId(2L);
        dto.setNom("modele2026.docx");
        dto.setDescription("Modèle pour l'année 2026");
        dto.setFormat("pdf");

        assertEquals(2L, dto.getId());
        assertEquals("modele2026.docx", dto.getNom());
        assertEquals("Modèle pour l'année 2026", dto.getDescription());
        assertEquals("pdf", dto.getFormat());
    }

    @Test
    void testToString() {
        ModeleDTOForList dto = new ModeleDTOForList(3L, "modele2027.docx", "Modèle 2027", "docx");

        String result = dto.toString();
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("nom='modele2027.docx'"));
        assertTrue(result.contains("description='Modèle 2027'"));
        assertTrue(result.contains("format='docx'"));
    }
}