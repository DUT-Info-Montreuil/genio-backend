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
}