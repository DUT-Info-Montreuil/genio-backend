package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurDTOTest {

    @Test
    void testGettersAndSetters() {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setUsername("jdupont");
        dto.setMotDePasse("password123");

        assertEquals("Dupont", dto.getNom());
        assertEquals("Jean", dto.getPrenom());
        assertEquals("jdupont", dto.getUsername());
        assertEquals("password123", dto.getMotDePasse());
    }
}