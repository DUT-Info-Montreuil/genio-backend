package com.genio.dto.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class InputUtilisateurUpdateDTOTest {
    @Test
    void testGettersAndSetters() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Alice");
        dto.setRole("GESTIONNAIRE");
        dto.setActif(true);

        assertEquals("Dupont", dto.getNom());
        assertEquals("Alice", dto.getPrenom());
        assertEquals("GESTIONNAIRE", dto.getRole());
        assertTrue(dto.getActif());
    }

    @Test
    void testToString() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setNom("Martin");
        dto.setPrenom("Paul");
        dto.setRole("ADMIN");
        dto.setActif(false);

        String result = dto.toString();
        assertTrue(result.contains("Martin"));
        assertTrue(result.contains("Paul"));
        assertTrue(result.contains("ADMIN"));
        assertTrue(result.contains("false"));
    }

    @Test
    void testDefaultValues() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        assertNull(dto.getNom());
        assertNull(dto.getPrenom());
        assertNull(dto.getRole());
        assertNull(dto.getActif());
    }

    @Test
    void testEqualsAndHashCode() {
        UtilisateurUpdateDTO dto1 = new UtilisateurUpdateDTO();
        dto1.setNom("Dupont");
        dto1.setPrenom("Alice");
        dto1.setRole("USER");
        dto1.setActif(true);

        UtilisateurUpdateDTO dto2 = new UtilisateurUpdateDTO();
        dto2.setNom("Dupont");
        dto2.setPrenom("Alice");
        dto2.setRole("USER");
        dto2.setActif(true);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
