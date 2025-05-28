package com.genio.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setNom("Martin");
        dto.setPrenom("Claire");
        dto.setRole("ADMIN");
        dto.setActif(true);

        assertEquals("Martin", dto.getNom());
        assertEquals("Claire", dto.getPrenom());
        assertEquals("ADMIN", dto.getRole());
        assertTrue(dto.getActif());
    }

    @Test
    void testToStringContainsAllFields() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setNom("Durand");
        dto.setPrenom("Alice");
        dto.setRole("USER");
        dto.setActif(false);

        String result = dto.toString();
        assertTrue(result.contains("Durand"));
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("USER"));
        assertTrue(result.contains("false"));
    }

    @Test
    void testDefaultValuesAreNull() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        assertNull(dto.getNom());
        assertNull(dto.getPrenom());
        assertNull(dto.getRole());
        assertNull(dto.getActif());
    }

    @Test
    void testActifTrueFalseSwitch() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setActif(true);
        assertTrue(dto.getActif());
        dto.setActif(false);
        assertFalse(dto.getActif());
    }

    @Test
    void testNomEtPrenomEquality() {
        UtilisateurUpdateDTO dto1 = new UtilisateurUpdateDTO();
        dto1.setNom("Lemoine");
        dto1.setPrenom("Julien");

        UtilisateurUpdateDTO dto2 = new UtilisateurUpdateDTO();
        dto2.setNom("Lemoine");
        dto2.setPrenom("Julien");

        assertEquals(dto1.getNom(), dto2.getNom());
        assertEquals(dto1.getPrenom(), dto2.getPrenom());
    }

    @Test
    void testToString() {
        UtilisateurUpdateDTO dto = new UtilisateurUpdateDTO();
        dto.setNom("Martin");
        dto.setPrenom("Luc");
        dto.setRole("USER");
        dto.setActif(false);

        String str = dto.toString();

        assertTrue(str.contains("UtilisateurUpdateDTO"));
        assertTrue(str.contains("nom='Martin'"));
        assertTrue(str.contains("prenom='Luc'"));
        assertTrue(str.contains("role='USER'"));
        assertTrue(str.contains("actif=false"));
    }
}