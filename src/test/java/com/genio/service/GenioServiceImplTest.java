package com.genio.service;

import com.genio.GenioServiceApplication;
import com.genio.dto.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.exception.GlobalExceptionHandler;
import com.genio.exception.business.InvalidFileFormatException;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.model.Modele;
import com.genio.model.Tuteur;
import com.genio.repository.ModeleRepository;
import com.genio.repository.TuteurRepository;
import com.genio.service.impl.DocxGenerator;
import com.genio.service.impl.GenioServiceImpl;
import com.genio.service.impl.HistorisationService;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GenioServiceApplication.class)
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

    @MockBean
    private JavaMailSender javaMailSender;


    @Autowired
    private TuteurRepository tuteurRepository;

    @MockBean
    private HistorisationService historisationService;

    @BeforeEach
    void setup() {
        modeleRepository.deleteAll();

        Modele modele = new Modele();
        modele.setNom("Test Modele");
        modele.setAnnee("2025");

        byte[] dummyFile = "dummy-content".getBytes();
        modele.setFichierBinaire(dummyFile);
        modele.setFichierHash(modeleService.generateFileHash(dummyFile));

        modele.setTitre("Titre de test");

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
        modele.setFichierHash("hash-factice");
        modele = modeleRepository.saveAndFlush(modele);

        when(docxGenerator.generateDocxFromTemplate(any(), any()))
                .thenReturn("fichier-mocke".getBytes());

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT2"));
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));


        Tuteur tuteur = new Tuteur();
        tuteur.setNom("TuteurNom");
        tuteur.setPrenom("TuteurPrenom");
        tuteur.setEmail("tuteur@example.com");
        tuteur = tuteurRepository.saveAndFlush(tuteur);
        input.setTuteur(new TuteurDTO(tuteur.getNom(), tuteur.getPrenom(), tuteur.getEmail()));

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
        modele.setNom("mocked-model.docx");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele.setFichierHash(modeleService.generateFileHash("dummy-docx-template".getBytes()));
        modele.setAnnee("2025");
        modele.setTitre("Titre de test");
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT1"));
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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", phone, "johndoe@example.com", "CPAM123","BUT2"));

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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT3"));

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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", null, "01.23.45.67.89", "johndoe@example.com", "CPAM123","BUT1"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse"));
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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT2"));

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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT1"));
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
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com", "CPAM123", "BUT1"));
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
                null,
                "01.23.45.67.89",
                "johndoe@example.com",
                "CPAM123",
                "BUT1"
        ));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse"));
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_modelNomNullEtPasDeFichier_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("placeholder.docx");
        modele.setFichierBinaire("dummy".getBytes());
        modele.setAnnee("2025");
        modele = modeleRepository.saveAndFlush(modele);

        modele.setNom(null);
        modele.setFichierBinaire(null);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "adresse", "0123456789", "john@example.com", "CPAM","BUT2"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : le modèle ne contient ni fichier binaire ni nom de fichier.", result.getMessageErreur());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_tuteurNonPersiste_shouldReturnError() {
        Modele modele = new Modele();
        modele.setNom("valid.docx");
        modele.setAnnee("2025");
        modele.setFichierBinaire("template".getBytes());
        modele = modeleRepository.saveAndFlush(modele);

        TuteurDTO tuteurDTO = new TuteurDTO(null, "Prenom", "email@example.com");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());

        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "adresse", "01.23.45.67.89", "john@example.com", "CPAM123","BUT3"));
        input.setTuteur(tuteurDTO);
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(new OrganismeDTO("Org", "Adresse", "Rep", "Qualité", "Service", "01.23.45.67.89", "org@example.com", "Lieu"));
        input.setStage(new StageDTO("2022", "Sujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("Le nom de l'enseignant doit être une chaîne alphabétique"));
        assertTrue(result.getMessageErreur().contains("Le nom de l'enseignant est manquant"));
    }



    @Test
    void getModelesByAnnee_success() {
        Modele modele = new Modele();
        modele.setNom("mocked-model.docx");
        modele.setFichierBinaire("dummy-docx-template".getBytes());
        modele.setFichierHash(modeleService.generateFileHash("dummy-docx-template".getBytes()));
        modele.setAnnee("2026");
        modele.setTitre("Titre de test");
        modele = modeleRepository.saveAndFlush(modele);

        List<Modele> modeles = genioService.getModelesByAnnee("2026");

        assertFalse(modeles.isEmpty());
        assertEquals("2026", modeles.get(0).getAnnee());
    }

    @Test
    void getModelesByAnnee_modelNotFound_shouldThrowException() {
        assertThrows(ModelNotFoundException.class, () -> genioService.getModelesByAnnee("1900"));
    }
}