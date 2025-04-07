package com.genio.service;

import com.genio.dto.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.exception.GlobalExceptionHandler;
import com.genio.exception.business.InvalidFileFormatException;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.DocxGenerator;
import com.genio.service.impl.GenioServiceImpl;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

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

    @MockBean
    private DocxGenerator docxGenerator;

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
    @Transactional
    void generateConvention_validModel_shouldReturnSuccess() throws Exception {
        Modele modele = new Modele();
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele.setAnnee("2025");
        modele.setNom("mocked-model.docx");
        modele = modeleRepository.saveAndFlush(modele);

        when(docxGenerator.generateDocxFromTemplate(any(), any()))
                .thenReturn("fichier-mocke".getBytes());

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertTrue(result.isSuccess());
        assertNotNull(result.getFichierBinaire());
    }



    @ParameterizedTest
    @CsvSource({
            "TXT, Erreur : format de fichier non supporté.",
            "XML, Erreur : format de fichier non supporté.",
            "CSV, Erreur : format de fichier non supporté."
    })
    @Rollback
    @Transactional
    void generateConvention_invalidFileFormat_shouldReturnError(String format, String expectedMessage) {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(new OrganismeDTO("Nom", "Adresse", "Rep", "Qualité", "Service", "01.23.45.67.89", "orga@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "Sujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, format);

        assertFalse(result.isSuccess());
        assertEquals(expectedMessage, result.getMessageErreur());
    }



    @ParameterizedTest
    @ValueSource(strings = {"invalid-phone", "12345", "9876543210"})
    @Rollback
    @Transactional
    void generateConvention_invalidPhone_shouldReturnValidationError(String phone) {
        Modele modele = new Modele();
        modele.setNom("modeleValide.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("mock-template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", phone, "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("format"));
    }

    @Test
    void handleInvalidFileFormatException_shouldReturnBadRequest() {
        InvalidFileFormatException exception = new InvalidFileFormatException("Fichier format incorrect");

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidFileFormatException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fichier format incorrect", response.getBody());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_modelWithInvalidName_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("invalid-model.txt");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : le nom du modèle doit se terminer par .docx.", result.getMessageErreur());
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_invalidData_shouldReturnValidationError() {
        Modele modele = new Modele();
        modele.setNom("valid-model.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", null, "01.23.45.67.89", "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse"));
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_fileGenerationException_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("valid-model.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        when(docxGenerator.generateDocxFromTemplate(any(), any())).thenThrow(new RuntimeException("Erreur lors de la génération du fichier DOCX"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur inattendue : contacter l’administrateur.", result.getMessageErreur());
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_validModel_pdfFormat_shouldReturnSuccess() throws Exception {
        Modele modele = new Modele();
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele.setAnnee("2025");
        modele.setNom("mocked-model.docx");
        modele = modeleRepository.saveAndFlush(modele);

        when(docxGenerator.generateDocxFromTemplate(any(), any()))
                .thenReturn("fichier-mocke".getBytes());

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        ConventionBinaireRes result = genioService.generateConvention(input, "PDF");

        assertTrue(result.isSuccess());
        assertNotNull(result.getFichierBinaire());
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_fileGenerationError_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("valid-model.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        when(docxGenerator.generateDocxFromTemplate(any(), any())).thenThrow(new RuntimeException("File generation error"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur inattendue : contacter l’administrateur.", result.getMessageErreur());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_modelWithNullBinaryFile_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("modeleSansFichier.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire(null);
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Les erreurs suivantes ont été détectées : Le champ 'organisme' : Le nom de l'organisme est manquant., Le champ 'maitreDeStage' : Le champ 'maitreDeStage' est obligatoire., Le champ 'stage' : Le sujet du stage est manquant., Le champ 'tuteur' : Le nom de l'enseignant est manquant.", result.getMessageErreur());
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_missingStageSubject_shouldReturnValidationError() {
        Modele modele = new Modele();
        modele.setNom("valid-model.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setStage(new StageDTO("2022", null, "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("Le sujet du stage est manquant"));
    }

    @ParameterizedTest
    @ValueSource(longs = {999L, 1000L, 12345L})
    @Rollback
    @Transactional
    void generateConvention_modelNotFound_shouldReturnError(long modeleId) {
        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modeleId);

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : modèle introuvable avec l'ID " + modeleId, result.getMessageErreur());
    }


    @ParameterizedTest
    @CsvSource({
            "2025, mocked-bytes",
            "2024, mocked-bytes"
    })
    @Rollback
    @Transactional
    void generateConvention_modelWithDifferentYears_shouldReturnSuccess(String annee, String fichierRetourne) throws Exception {
        Modele modele = new Modele();
        modele.setNom("Modele " + annee + ".docx");
        modele.setAnnee(annee);
        modele.setFichierBinaire(fichierRetourne.getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123"));
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(new OrganismeDTO("Nom", "Adresse", "Rep", "Qualité", "Service", "01.23.45.67.89", "orga@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "Sujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "tuteur@example.com"));

        when(docxGenerator.generateDocxFromTemplate(any(), any())).thenReturn(fichierRetourne.getBytes());

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertTrue(result.isSuccess());
        assertNotNull(result.getFichierBinaire());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_missingRequiredFields_shouldReturnValidationError() {
        Modele modele = new Modele();
        modele.setNom("modele-test-valide.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("template-factice".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO(
                "John", "Doe", "H", "2000-01-01",
                null, // Par exemple, "adresse" est manquante ici
                "01.23.45.67.89",
                "johndoe@example.com",
                "CPAM123"
        ));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        // Vérification des erreurs de validation
        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse")); // S'assurer que l'erreur contient le champ manquant
    }
}