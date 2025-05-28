package com.genio.dto;

import com.genio.dto.input.ConventionServiceDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConventionServiceDTOTest {

    @Test
    void testSettersAndGetters() {
        EtudiantDTO etudiant = new EtudiantDTO(
                "Nom", "Prénom", "F", "2000-01-01", "1 rue Exemple", "01.02.03.04.05",
                "email@etu.com", "CPAM 75", "BUT1"
        );
        MaitreDeStageDTO maitre = new MaitreDeStageDTO(
                "Tuteur", "Prénom", "Dev", "01.01.01.01.01", "tuteur@mail.com");
        OrganismeDTO organisme = new OrganismeDTO(
                "Organisme", "Adresse", "Rep", "Directeur", "Service", "01.02.03.04.05", "org@mail.com", "Paris");
        StageDTO stage = StageDTO.builder()
                .anneeStage("2025")
                .sujetDuStage("Sujet")
                .dateDebutStage("2025-04-01")
                .dateFinStage("2025-07-31")
                .duree("4 mois")
                .joursTot(90)
                .heuresTot(630)
                .remunerationHoraire("15.00€")
                .saeStageProfessionnel("Oui")
                .build();
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
        dto.setEtudiant(new EtudiantDTO(
                "Nom", "Prénom", "F", "2000-01-01", "1 rue Exemple", "01.02.03.04.05",
                "email@etu.com", "CPAM", "BUT2"
        ));
        dto.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prénom", "Fonction", "01.02.03.04.05", "mail@mail.com"));
        dto.setOrganisme(new OrganismeDTO("Nom", "Adresse", "Rep", "Qualité", "Service", "01.02.03.04.05", "org@mail.com", "Paris"));
        dto.setStage(StageDTO.builder()
                .anneeStage("2024")
                .sujetDuStage("Sujet")
                .dateDebutStage("2024-04-01")
                .dateFinStage("2024-07-01")
                .duree("3 mois")
                .joursTot(60)
                .heuresTot(420)
                .remunerationHoraire("12.50€")
                .saeStageProfessionnel("Oui")
                .build()); dto.setTuteur(new TuteurDTO("Nom", "Prénom", "tuteur@mail.com"));
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