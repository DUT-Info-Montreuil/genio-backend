package com.genio.mapper;

import com.genio.dto.*;
import com.genio.dto.input.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionMapperTest {

    @Test
    void testToServiceDTO() {
        EtudiantDTO etudiant = EtudiantDTO.builder()
                .nom("Dupont")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("john.doe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build();

        MaitreDeStageDTO maitre = new MaitreDeStageDTO(
                "Martin", "Paul", "Responsable IT", "01.23.45.67.89", "paul@example.com"
        );

        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("Entreprise")
                .adresse("Adresse")
                .nomRepresentant("Représentant")
                .qualiteRepresentant("Directeur")
                .nomDuService("Service")
                .telephone("01.02.03.04.05")
                .email("mail@org.com")
                .lieuDuStage("Paris")
                .build();

        StageDTO stage = new StageDTO(
                "2025",
                "Développement logiciel",
                "2025-03-01",
                "2025-06-30",
                "4 mois",
                90,
                630,
                "12€/h",
                "Oui"
        );
        TuteurDTO tuteur = new TuteurDTO("Durand", "Luc", "luc.durand@iut.fr");

        Long modeleId = 1L;

        ConventionWsDTO wsDTO = new ConventionWsDTO(etudiant, maitre, organisme, stage, tuteur, modeleId);

        ConventionServiceDTO serviceDTO = ConventionMapper.toServiceDTO(wsDTO);

        assertNotNull(serviceDTO);
        assertEquals("Dupont", serviceDTO.getEtudiant().getNom());
        assertEquals("Paul", serviceDTO.getMaitreDeStage().getPrenom());
        assertEquals("Entreprise", serviceDTO.getOrganisme().getNom());
        assertEquals("Développement logiciel", serviceDTO.getStage().getSujetDuStage());
        assertEquals("Durand", serviceDTO.getTuteur().getNom());
        assertEquals(1L, serviceDTO.getModeleId());
    }

    @Test
    void testToServiceDTO_ShouldThrowException_WhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ConventionMapper.toServiceDTO(null));

        assertEquals("Les données d'entrée sont nulles", exception.getMessage());
    }
}