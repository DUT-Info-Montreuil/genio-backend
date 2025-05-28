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
import java.util.UUID;

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

        byte[] dummyFile = ("dummy-content-" + UUID.randomUUID()).getBytes();
        modele.setFichierBinaire(dummyFile);
        modele.setFichierHash(modeleService.generateFileHash(dummyFile));

        modele.setTitre("Titre de test");

        modeleRepository.saveAndFlush(modele);
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_validModel_shouldReturnSuccess() throws Exception {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        when(docxGenerator.generateDocxFromTemplate(any(), any()))
                .thenReturn("fichier-mocke".getBytes());

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setMaitreDeStage(new MaitreDeStageDTO("MaitreDeStageNom", "MaitreDeStagePrenom", "Fonction", "01.23.45.67.89", "maitreDeStage@example.com"));
        input.setOrganisme(OrganismeDTO.builder()
                .nom("Organisme")
                .adresse("Adresse")
                .nomRepresentant("RepNom")
                .qualiteRepresentant("RepQualite")
                .nomDuService("Service")
                .telephone("01.23.45.67.89")
                .email("organisme@example.com")
                .lieuDuStage("Lieu")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2022")
                .sujetDuStage("StageSujet")
                .dateDebutStage("2022-01-01")
                .dateFinStage("2022-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
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
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(OrganismeDTO.builder()
                .nom("Nom")
                .adresse("Adresse")
                .nomRepresentant("Rep")
                .qualiteRepresentant("Qualite")
                .nomDuService("Service")
                .telephone("01.23.45.67.89")
                .email("organisme@example.com")
                .lieuDuStage("Lieu")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2022")
                .sujetDuStage("Sujet")
                .dateDebutStage("2022-01-01")
                .dateFinStage("2022-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
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
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        EtudiantDTO etudiantDTO = EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone(phone)
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build();

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(etudiantDTO);

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        System.out.println("Message erreur : " + result.getMessageErreur());

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().toLowerCase().contains("telephone"));
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
        // Création d’un modèle avec un nom invalide (pas .docx)
        Modele modele = new Modele();
        modele.setNom("fichier_invalide.txt"); // <-- nom incorrect
        modele.setAnnee("2025");
        modele.setTitre("Titre test");
        modele.setFichierBinaire("fake content".getBytes());
        modele.setFichierHash(modeleService.generateFileHash("fake content".getBytes()));
        modele = modeleRepository.saveAndFlush(modele);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setTuteur(new TuteurDTO("NomTuteur", "PrenomTuteur", "tuteur@example.com"));
        input.setMaitreDeStage(new MaitreDeStageDTO("NomMDS", "PrenomMDS", "Fonction", "01.23.45.67.89", "mds@example.com"));
        input.setOrganisme(OrganismeDTO.builder()
                .nom("OrgName")
                .adresse("Adresse")
                .nomRepresentant("Rep")
                .qualiteRepresentant("Qualite")
                .nomDuService("Service")
                .telephone("01.23.45.67.89")
                .email("org@example.com")
                .lieuDuStage("Lieu")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2025")
                .sujetDuStage("Sujet")
                .dateDebutStage("2025-01-01")
                .dateFinStage("2025-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
       ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : le nom du modèle doit se terminer par .docx.", result.getMessageErreur());
    }
    @Test
    @Rollback
    @Transactional
    void generateConvention_invalidData_shouldReturnValidationError() {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse"));
    }




    @Test
    @Rollback
    @Transactional
    void generateConvention_modelWithNullBinaryFile_shouldReturnError() {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals(
                "Les erreurs suivantes ont été détectées : Le champ 'organisme' : Le nom de l'organisme est manquant., Le champ 'maitreDeStage' : Le nom du tuteur est manquant., Le champ 'stage' : Le sujet du stage est manquant., Le champ 'tuteur' : Le nom de l'enseignant est manquant.",
                result.getMessageErreur()
        );
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_missingStageSubject_shouldReturnValidationError() {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2022")
                .sujetDuStage(null)
                .dateDebutStage("2022-01-01")
                .dateFinStage("2022-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
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
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(OrganismeDTO.builder()
                .nom("Organisme")
                .adresse("Adresse")
                .nomRepresentant("RepNom")
                .qualiteRepresentant("RepQualite")
                .nomDuService("Service")
                .telephone("01.23.45.67.89")
                .email("organisme@example.com")
                .lieuDuStage("Lieu")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2022")
                .sujetDuStage("Sujet")
                .dateDebutStage("2022-01-01")
                .dateFinStage("2022-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
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
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());

        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("adresse"));
    }

    @Test
    @Rollback
    @Transactional
    void generateConvention_modelNomNullEtPasDeFichier_shouldReturnError() {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2025");

        modele.setNom(null);
        modele.setFichierBinaire(null);

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertEquals("Erreur : le modèle ne contient ni fichier binaire ni nom de fichier.", result.getMessageErreur());
    }


    @Test
    @Rollback
    @Transactional
    void generateConvention_tuteurNonPersiste_shouldReturnError() {
        Modele modele = TestUtils.createUniqueTestModele(modeleService, modeleRepository, "2026");
        TuteurDTO tuteurDTO = new TuteurDTO(null, "Prenom", "email@example.com");

        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(modele.getId());
        input.setEtudiant(EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("johndoe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build());
        input.setTuteur(tuteurDTO); // 👈 Nom est null ici
        input.setMaitreDeStage(new MaitreDeStageDTO("Nom", "Prenom", "Fonction", "01.23.45.67.89", "mail@example.com"));
        input.setOrganisme(OrganismeDTO.builder()
                .nom("Organisme")
                .adresse("Adresse")
                .nomRepresentant("RepNom")
                .qualiteRepresentant("RepQualite")
                .nomDuService("Service")
                .telephone("01.23.45.67.89")
                .email("organisme@example.com")
                .lieuDuStage("Lieu")
                .build());
        input.setStage(StageDTO.builder()
                .anneeStage("2022")
                .sujetDuStage("Sujet")
                .dateDebutStage("2022-01-01")
                .dateFinStage("2022-06-30")
                .duree("5 mois")
                .joursTot(20)
                .heuresTot(200)
                .remunerationHoraire("10€")
                .saeStageProfessionnel("professionnel")
                .build());
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("Le champ 'tuteur.nom'")); // 💡 Ce champ est bien dans le message
        assertTrue(result.getMessageErreur().contains("une chaîne alphabétique")); // 💡 Vérifie le vrai message
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