package com.genio.dto.input;

import com.genio.dto.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionWsDTOTest {

    @Test
    void testConstructorAndGetters() {
        EtudiantDTO etudiant = EtudiantDTO.builder()
                .nom("Dupont")
                .prenom("Marie")
                .email("marie.durand@example.com")
                .adresse("123 Rue Principale")
                .telephone("01.23.45.67.89")
                .sexe("H")
                .dateNaissance("1999-05-21")
                .cpam("CPAM Paris")
                .promotion("BUT2")
                .build();

        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("Entreprise X")
                .adresse("456 Rue des Champs")
                .nomRepresentant("Julie Blanc")
                .qualiteRepresentant("Directrice")
                .nomDuService("Informatique")
                .telephone("0789456123")
                .email("contact@entreprise-x.com")
                .lieuDuStage("Paris")
                .build();

        MaitreDeStageDTO maitre = new MaitreDeStageDTO("Martin", "Paul", "paul.martin@example.com", "Responsable", "0987654321");
        StageDTO stage = new StageDTO("Développement d'une appli web", "2025-01-15", "2025-07-15", "6 mois", 180, 720, "15€", "Professionnel", "2025");
        TuteurDTO tuteur = new TuteurDTO("Durand", "Marie", "marie.durand@example.com");
        Long modeleId = 42L;

        ConventionWsDTO dto = new ConventionWsDTO(etudiant, maitre, organisme, stage, tuteur, modeleId);

        assertEquals(etudiant, dto.getEtudiant());
        assertEquals(maitre, dto.getMaitreDeStage());
        assertEquals(organisme, dto.getOrganisme());
        assertEquals(stage, dto.getStage());
        assertEquals(tuteur, dto.getTuteur());
        assertEquals(modeleId, dto.getModeleId());
    }

    @Test
    void testSettersAndGetters() {
        ConventionWsDTO dto = new ConventionWsDTO(null, null, null, null, null, null);

        EtudiantDTO etudiant = EtudiantDTO.builder()
                .nom("Dupont")
                .prenom("Marie")
                .email("marie.durand@example.com")
                .adresse("123 Rue Principale")
                .telephone("0123456789")
                .sexe("F")
                .dateNaissance("1999-05-21")
                .cpam("CPAM Paris")
                .promotion("BUT2")
                .build();

        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("Entreprise X")
                .adresse("456 Rue des Champs")
                .nomRepresentant("Julie Blanc")
                .qualiteRepresentant("Directrice")
                .nomDuService("Informatique")
                .telephone("0789456123")
                .email("contact@entreprise-x.com")
                .lieuDuStage("Paris")
                .build();

        MaitreDeStageDTO maitre = new MaitreDeStageDTO("Martin", "Paul", "paul.martin@example.com", "Responsable", "0987654321");
        StageDTO stage = new StageDTO("Développement d'une appli web", "2025-01-15", "2025-07-15", "6 mois", 180, 720, "15€", "Professionnel", "2025");
        TuteurDTO tuteur = new TuteurDTO("Durand", "Marie", "marie.durand@example.com");
        Long modeleId = 99L;

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
    void testToString() {
        ConventionWsDTO dto = new ConventionWsDTO(
                EtudiantDTO.builder()
                        .nom("Dupont")
                        .prenom("Marie")
                        .email("marie.durand@example.com")
                        .adresse("123 Rue Principale")
                        .telephone("0123456789")
                        .sexe("F")
                        .dateNaissance("1999-05-21")
                        .cpam("CPAM Paris")
                        .promotion("BUT2")
                        .build(),
                new MaitreDeStageDTO("Martin", "Paul", "paul.martin@example.com", "Responsable", "0987654321"),
                OrganismeDTO.builder()
                        .nom("Entreprise X")
                        .adresse("456 Rue des Champs")
                        .nomRepresentant("Julie Blanc")
                        .qualiteRepresentant("Directrice")
                        .nomDuService("Informatique")
                        .telephone("0789456123")
                        .email("contact@entreprise-x.com")
                        .lieuDuStage("Paris")
                        .build(),
                new StageDTO("Développement d'une appli web", "2025-01-15", "2025-07-15", "6 mois", 180, 720, "15€", "Professionnel", "2025"),
                new TuteurDTO("Durand", "Marie", "marie.durand@example.com"),
                123L
        );

        String output = dto.toString();
        assertTrue(output.contains("modeleId=123"));
        assertTrue(output.contains("etudiant="));
        assertTrue(output.contains("maitreDeStage="));
        assertTrue(output.contains("organisme="));
        assertTrue(output.contains("stage="));
        assertTrue(output.contains("tuteur="));
    }
}