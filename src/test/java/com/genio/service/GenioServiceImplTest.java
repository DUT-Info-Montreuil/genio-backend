package com.genio.service;

import com.genio.dto.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.GenioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GenioServiceImplTest {

    @Autowired
    private GenioServiceImpl genioService;

    @Autowired
    private ModeleRepository modeleRepository;

    @BeforeEach
    void setup() {
        modeleRepository.deleteAll();
        Modele modele = new Modele();
        modele.setNom("Test Modele");
        modele.setAnnee("2025");
        modeleRepository.saveAndFlush(modele);
    }

    @Test
    @Rollback
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void generateConvention_modelNotFound_shouldThrowException() {
        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(999L); // ID inexistant

        System.out.println("Test avec un ID inexistant (999)");

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : modèle introuvable avec l'ID 999", result.getMessageErreur());
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_validModel_shouldReturnSuccess() throws Exception {

        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        System.out.println("Modele enregistré avec ID: " + modele.getId());

        // Vérification que le modèle a bien été sauvegardé avec une année non nulle
        assertNotNull(modele.getId(), "L'ID du modèle est null après l'insertion en base !");
        assertNotNull(modele.getAnnee(), "L'année du modèle est null après l'insertion en base !");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId()); // Utilise l'ID du modèle inséré
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        // Ajout d'un log pour voir si l'ID est bien utilisé
        System.out.println("Test avec modèle existant, ID = " + modele.getId());

        // Exécution du test
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        // Vérification des résultats
        assertTrue(result.isSuccess(), "La convention générée devrait être un succès !");
        assertNotNull(result.getFichierBinaire(), "Le fichier binaire généré ne doit pas être null !");
    }
}