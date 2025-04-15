package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaitreDeStageDTOTest {

    @Test
    void testConstructorAndGetters() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("Martin", "Paul", "Chef de projet", "01.02.03.04.05", "paul.martin@example.com");

        assertEquals("Martin", dto.getNom());
        assertEquals("Paul", dto.getPrenom());
        assertEquals("Chef de projet", dto.getFonction());
        assertEquals("01.02.03.04.05", dto.getTelephone());
        assertEquals("paul.martin@example.com", dto.getEmail());
    }

    @Test
    void testSetNom() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("X", "Y", "Z", "01.01.01.01.01", "x@y.com");
        dto.setNom("Bernard");
        assertEquals("Bernard", dto.getNom());
    }

    @Test
    void testToString() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("Nom", "Prénom", "Fonction", "02.03.04.05.06", "email@example.com");
        String result = dto.toString();
        assertTrue(result.contains("Nom"));
        assertTrue(result.contains("Prénom"));
        assertTrue(result.contains("Fonction"));
        assertTrue(result.contains("02.03.04.05.06"));
        assertTrue(result.contains("email@example.com"));
    }
}