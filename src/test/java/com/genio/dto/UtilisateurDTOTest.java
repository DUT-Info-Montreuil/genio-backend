package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurDTOTest {

    @Test
    void testGettersAndSetters() {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setEmail("jdupont");
        dto.setMotDePasse("password123");

        assertEquals("Dupont", dto.getNom());
        assertEquals("Jean", dto.getPrenom());
        assertEquals("jdupont", dto.getEmail());
        assertEquals("password123", dto.getMotDePasse());
    }
}