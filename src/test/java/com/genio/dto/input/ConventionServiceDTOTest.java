package com.genio.dto.input;

import com.genio.dto.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConventionServiceDTOTest {

    @Test
    void testSettersAndGetters() {
        EtudiantDTO etudiant = EtudiantDTO.builder()
                .nom("Nom")
                .prenom("Prénom")
                .sexe("F")
                .dateNaissance("2000-01-01")
                .adresse("1 rue Exemple")
                .telephone("01.02.03.04.05")
                .email("email@etu.com")
                .cpam("CPAM 75") // <-- ICI
                .promotion("BUT1")
                .build();

        MaitreDeStageDTO maitre = new MaitreDeStageDTO(
                "Tuteur", "Prénom", "Dev", "01.01.01.01.01", "tuteur@mail.com");

        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("Organisme")
                .adresse("Adresse")
                .nomRepresentant("Rep")
                .qualiteRepresentant("Directeur")
                .nomDuService("Service")
                .telephone("01.02.03.04.05")
                .email("org@mail.com")
                .lieuDuStage("Paris")
                .build();

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

        dto.setEtudiant(EtudiantDTO.builder()
                .nom("Nom")
                .prenom("Prénom")
                .sexe("F")
                .dateNaissance("2000-01-01")
                .adresse("1 rue Exemple")
                .telephone("01.02.03.04.05")
                .email("email@etu.com")
                .cpam("CPAM") // <-- ICI
                .promotion("BUT2")
                .build());

        dto.setMaitreDeStage(new MaitreDeStageDTO(
                "Nom", "Prénom", "Fonction", "01.02.03.04.05", "mail@mail.com"));

        dto.setOrganisme(OrganismeDTO.builder()
                .nom("Nom")
                .adresse("Adresse")
                .nomRepresentant("Rep")
                .qualiteRepresentant("Qualité")
                .nomDuService("Service")
                .telephone("01.02.03.04.05")
                .email("org@mail.com")
                .lieuDuStage("Paris")
                .build());

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
                .build());

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