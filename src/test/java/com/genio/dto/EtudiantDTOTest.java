package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EtudiantDTOTest {

    @Test
    void testConstructorAndGetters() {
        EtudiantDTO dto = new EtudiantDTO(
                "Durand",
                "Sophie",
                "F",
                "2000-05-14",
                "12 rue des Lilas",
                "06.12.34.56.78",
                "sophie.durand@example.com",
                "CPAM 75"
        );

        assertEquals("Durand", dto.getNom());
        assertEquals("Sophie", dto.getPrenom());
        assertEquals("F", dto.getSexe());
        assertEquals("2000-05-14", dto.getDateNaissance());
        assertEquals("12 rue des Lilas", dto.getAdresse());
        assertEquals("06.12.34.56.78", dto.getTelephone());
        assertEquals("sophie.durand@example.com", dto.getEmail());
        assertEquals("CPAM 75", dto.getCpam());
    }

    @Test
    void testSetters() {
        EtudiantDTO dto = new EtudiantDTO(
                "NomTest", "PrenomTest", "H", "1999-01-01",
                "AdresseTest", "01.02.03.04.05", "test@example.com", "CPAMTEST"
        );

        dto.setNom("Lemoine");
        dto.setPrenom("Julien");
        dto.setSexe("H");
        dto.setDateNaissance("1998-12-31");
        dto.setAdresse("45 avenue Victor Hugo");
        dto.setTelephone("07.89.56.12.34");
        dto.setEmail("julien.lemoine@mail.fr");
        dto.setCpam("CPAM 92");

        assertEquals("Lemoine", dto.getNom());
        assertEquals("Julien", dto.getPrenom());
        assertEquals("H", dto.getSexe());
        assertEquals("1998-12-31", dto.getDateNaissance());
        assertEquals("45 avenue Victor Hugo", dto.getAdresse());
        assertEquals("07.89.56.12.34", dto.getTelephone());
        assertEquals("julien.lemoine@mail.fr", dto.getEmail());
        assertEquals("CPAM 92", dto.getCpam());
    }

    @Test
    void testToString() {
        EtudiantDTO dto = new EtudiantDTO(
                "Martin", "Claire", "F", "2002-09-10",
                "5 rue du Général", "06.66.66.66.66", "claire.martin@etu.fr", "CPAM 33"
        );

        String result = dto.toString();

        assertTrue(result.contains("Martin"));
        assertTrue(result.contains("Claire"));
        assertTrue(result.contains("2002-09-10"));
        assertTrue(result.contains("claire.martin@etu.fr"));
        assertTrue(result.contains("CPAM 33"));
    }
}