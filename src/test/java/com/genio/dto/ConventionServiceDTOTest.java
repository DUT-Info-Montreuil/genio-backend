package com.genio.dto;

import com.genio.dto.input.ConventionServiceDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConventionServiceDTOTest {

    @Test
    void testSettersAndGetters() {
        EtudiantDTO etudiant = new EtudiantDTO(
                "Nom", "Prénom", "F", "2000-01-01", "1 rue Exemple", "01.02.03.04.05", "email@etu.com", "CPAM 75");
        MaitreDeStageDTO maitre = new MaitreDeStageDTO(
                "Tuteur", "Prénom", "Dev", "01.01.01.01.01", "tuteur@mail.com");
        OrganismeDTO organisme = new OrganismeDTO(
                "Organisme", "Adresse", "Rep", "Directeur", "Service", "01.02.03.04.05", "org@mail.com", "Paris");
        StageDTO stage = new StageDTO(
                "2025", "Sujet", "2025-04-01", "2025-07-31", "4 mois", 90, 630, "15.00€", "Oui");
        TuteurDTO tuteur = new TuteurDTO("Prof", "Marie", "prof@mail.com");

        Long modeleId = 123L;

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setEtudiant(etudiant);
        dto.setMaitreDeStage(maitre);
        dto.setOrganisme(organisme);
        dto.setStage(stage);
        dto.setTuteur(tuteur);
        dto.setModeleId(modeleId);

        assertEquals(etudiant, dto.getEtudiant());
        assertEquals(maitre, dto.getMaitreDeStage());
        assertEquals(organisme, dto.getOrganisme());
        assertEquals(stage, dto.getStage());
        assertEquals(tuteur, dto.getTuteur());
        assertEquals(modeleId, dto.getModeleId());
    }

    @Test
    void testToString_containsAllFields() {
        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setEtudiant(new EtudiantDTO("Nom", "Prénom", "F", "2000-01-01", "1 rue Exemple", "01.02.03.04.05", "email@etu.com", "CPAM"));
        dto.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prénom", "Fonction", "01.02.03.04.05", "mail@mail.com"));
        dto.setOrganisme(new OrganismeDTO("Nom", "Adresse", "Rep", "Qualité", "Service", "01.02.03.04.05", "org@mail.com", "Paris"));
        dto.setStage(new StageDTO("2024", "Sujet", "2024-04-01", "2024-07-01", "3 mois", 60, 420, "12.50€", "Oui"));
        dto.setTuteur(new TuteurDTO("Nom", "Prénom", "tuteur@mail.com"));
        dto.setModeleId(456L);

        String str = dto.toString();
        assertTrue(str.contains("etudiant"));
        assertTrue(str.contains("maitreDeStage"));
        assertTrue(str.contains("organisme"));
        assertTrue(str.contains("stage"));
        assertTrue(str.contains("tuteur"));
        assertTrue(str.contains("456"));
    }
}