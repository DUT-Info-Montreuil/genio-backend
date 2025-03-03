package com.genio.service;

import com.genio.dto.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.exception.GlobalExceptionHandler;
import com.genio.exception.business.DatabaseInsertionException;
import com.genio.exception.business.EmptyDirectoryException;
import com.genio.exception.business.EmptyFileException;
import com.genio.exception.business.InvalidFileFormatException;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.GenioServiceImpl;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GenioServiceImplTest {

    @Autowired
    private GenioServiceImpl genioService;

    @Autowired
    private ModeleRepository modeleRepository;

    @Autowired
    private ModeleService modeleService;


    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

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
        input.setModeleId(999L);

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

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertTrue(result.isSuccess(), "La convention générée devrait être un succès !");
        assertNotNull(result.getFichierBinaire(), "Le fichier binaire généré ne doit pas être null !");
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_missingRequiredFields_shouldReturnValidationError() {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", null, "01.23.45.67.89", "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess(), "La convention devrait échouer en raison de l'adresse manquante !");
        assertTrue(result.getMessageErreur().contains("Le champ 'etudiant.adresse' : L'adresse de l'étudiant est manquante."), "Le message d'erreur ne correspond pas à l'attendu");
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_invalidFileFormat_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, "TXT");

        assertFalse(result.isSuccess(), "La génération de la convention devrait échouer en raison du mauvais format !");
        assertEquals("Erreur : format de fichier non supporté.", result.getMessageErreur());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_modelWithDifferentYears_shouldReturnSuccess() {
        Modele modele = new Modele();
        modele.setNom("Modele 2025");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertTrue(result.isSuccess(), "La génération de la convention avec le modèle 2025 devrait réussir !");
        assertNotNull(result.getFichierBinaire(), "Le fichier binaire généré ne doit pas être null !");
    }
    @Test
    @Rollback
    @Transactional
    void generateConvention_invalidPhone_shouldReturnValidationError() {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "invalid-phone", "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess(), "La convention devrait échouer en raison du téléphone invalide !");
        assertTrue(result.getMessageErreur().contains("Le téléphone de l'étudiant doit être au format XX.XX.XX.XX.XX."), "Le message d'erreur ne correspond pas à l'attendu");
    }


    @Test
    void handleInvalidFileFormatException_shouldReturnBadRequest() {
        InvalidFileFormatException exception = new InvalidFileFormatException("Fichier format incorrect");

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidFileFormatException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fichier format incorrect", response.getBody());
    }

 /**   @Test
    @Rollback
    @Transactional
    void insertModele_emptyFile_shouldThrowEmptyFileException() throws IOException {
        Path tempFile = Files.createTempFile("emptyFile", ".docx");

        File emptyFile = tempFile.toFile();
        if (emptyFile.length() != 0) {
            Files.write(tempFile, new byte[0]);
        }

        EmptyFileException exception = assertThrows(EmptyFileException.class, () -> {
            modeleService.insertModele(emptyFile);
        });
        assertEquals("Le fichier " + emptyFile.getName() + " est vide.", exception.getMessage());
    }

    @Test
    @Rollback
    @Transactional
    void insertModele_invalidFileFormat_shouldThrowInvalidFileFormatException() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");

        File invalidFile = new File(tempDir.toFile(), "test_invalide.txt");
        if (!invalidFile.createNewFile()) {
            throw new IOException("Impossible de créer le fichier de test.");
        }
        Files.write(invalidFile.toPath(), "Contenu invalide".getBytes());

        Exception exception = assertThrows(InvalidFileFormatException.class, () -> {
            modeleService.insertModele(invalidFile);
        });

        assertTrue(exception.getMessage().contains("ne respecte pas le format attendu"));
    }*/

}