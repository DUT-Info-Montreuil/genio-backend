/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.controller;

import com.genio.dto.outputmodeles.ModeleDTO;
import com.genio.dto.outputmodeles.ModeleDTOForList;
import com.genio.mapper.DocxParser;
import com.genio.model.*;
import com.genio.repository.*;
import com.genio.service.impl.ModeleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ModeleControllerTest {

    @Autowired
    private ModeleController modeleController;

    @Autowired
    private ModeleService modeleService;

    @Autowired
    private ModeleRepository modeleRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private MaitreDeStageRepository maitreDeStageRepository;

    @MockBean
    private DocxParser docxParser;

    @MockBean
    private JavaMailSender javaMailSender;


    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ConventionRepository conventionRepository;

    @Value("${modele.conventionServices.directory}")
    private String directoryPath;

    @BeforeEach
    void setup() throws IOException {
        modeleRepository.deleteAll();

        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".docx"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            System.err.println("Erreur lors de la suppression du fichier : " + p + " -> " + e.getMessage());
                        }
                    });
        }
    }





    @Test
    @Rollback
    void testCreateModelConvention_Success() throws IOException {
        byte[] fileContent = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "file",
                "modeleConvention_2025.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                fileContent
        );

        when(docxParser.extractVariables(any())).thenReturn(List.of(
                "annee", "NOM_ORGANISME", "ADR_ORGANISME", "NOM_REPRESENTANT_ORG",
                "QUAL_REPRESENTANT_ORG", "NOM_DU_SERVICE", "TEL_ORGANISME", "MEL_ORGANISME",
                "LIEU_DU_STAGE", "NOM_ETUDIANT1", "PRENOM_ETUDIANT", "SEXE_ETUDIANT",
                "DATE_NAIS_ETUDIANT", "ADR_ETUDIANT", "TEL_ETUDIANT", "MEL_ETUDIANT",
                "SUJET_DU_STAGE", "DATE_DEBUT_STAGE", "DATE_FIN_STAGE", "STA_DUREE",
                "_STA_JOURS_TOT", "_STA_HEURES_TOT", "TUT_IUT", "TUT_IUT_MEL",
                "PRENOM_ENCADRANT", "NOM_ENCADRANT", "FONCTION_ENCADRANT",
                "TEL_ENCADRANT", "MEL_ENCADRANT", "NOM_CPAM", "Stage_Professionnel", "STA_REMU_HOR"
        ));

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre exemple");

        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("a été ajouté avec succès"));
    }

    @Test
    @Rollback
    void testCreateModelConvention_ModelAlreadyExists() throws IOException {
        byte[] fileContent = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "file",
                "modeleConvention_2025.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                fileContent
        );

        String hash = modeleService.generateFileHash(fileContent);

        Modele modele = new Modele();
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");
        modele.setFichierHash(hash);
        modele.setTitre("Titre original");
        modeleRepository.saveAndFlush(modele);

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre exemple");

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("déjà"));
    }

    @Test
    @Rollback
    void testCreateModelConvention_InvalidFileFormat() throws IOException {
        byte[] fileContent = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile("file", "modeleConvention.txt", "text/plain", fileContent);

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre exemple");

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Format non supporté, uniquement .docx accepté"));
    }

    @Test
    @Rollback
    void testGetAllModelConvention() {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modele.setFichierHash("hash-fictif");
        modele.setTitre("Titre original");
        modeleRepository.saveAndFlush(modele);

        ResponseEntity<?> response = modeleController.getAllModelConvention();

        assertEquals(200, response.getStatusCode().value());
        List<ModeleDTOForList> modeles = (List<ModeleDTOForList>) response.getBody();
        modeleRepository.saveAndFlush(modele);
    }

    @Test
    @Rollback
    void testGetModelConventionById_NotFound() {
        ResponseEntity<?> response = modeleController.getModelConventionById(999L);

        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Modèle introuvable"));
    }

    @Test
    @Rollback
    void testDeleteModelConvention_Success() {
        Modele modele = new Modele();
        modele.setNom("Modele à supprimer");
        modele.setAnnee("2025");
        modele.setFichierHash("hash-fictif");
        modele.setTitre("Titre test");

        modele = modeleRepository.saveAndFlush(modele);

        ResponseEntity<?> response = modeleController.archiveModelConvention(modele.getId());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Modèle archivé avec succès"));
    }

    @Test
    @Rollback
    void testDeleteModelConvention_NotFound() {
        ResponseEntity<?> response = modeleController.archiveModelConvention(999L);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Modèle introuvable"));
    }

    @Test
    @Rollback
    @Transactional
    void testDeleteModelConvention_ModelInUse() {
        Modele modele = new Modele();
        modele.setNom("Modele en utilisation");
        modele.setAnnee("2025");
        modele.setFichierHash("mockedHash");
        modele.setTitre("Titre test");
        modele = modeleRepository.saveAndFlush(modele);

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Dupont");
        etudiant.setPrenom("Marie");
        etudiant.setEmail("marie.durand@example.com");
        etudiant.setPromotion("2024");
        etudiant = etudiantRepository.saveAndFlush(etudiant);

        MaitreDeStage maitre = new MaitreDeStage();
        maitre.setNom("Martin");
        maitre.setPrenom("Paul");
        maitre.setEmail("paul.martin@example.com");
        maitre = maitreDeStageRepository.saveAndFlush(maitre);

        Tuteur tuteur = new Tuteur();
        tuteur.setNom("Durand");
        tuteur.setPrenom("Luc");
        tuteur.setEmail("luc.durand@example.com");
        tuteur = tuteurRepository.saveAndFlush(tuteur);

        Convention convention = new Convention();
        convention.setEtudiant(etudiant);
        convention.setMaitreDeStage(maitre);
        convention.setTuteur(tuteur);
        convention.setModele(modele);
        conventionRepository.saveAndFlush(convention);

        ResponseEntity<?> response = modeleController.archiveModelConvention(modele.getId());

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Le modèle est toujours utilisé"));
    }

    @Test
    @Rollback
    void testGetModelConventionById_Success() {
        Modele modele = new Modele();
        modele.setNom("Test");
        modele.setAnnee("2025");
        modele.setFichierHash("hash-fictif");
        modele.setTitre("Titre test");
        modele = modeleRepository.saveAndFlush(modele);

        ResponseEntity<?> response = modeleController.getModelConventionById(modele.getId());

        assertEquals(200, response.getStatusCode().value());
    }


    @Test
    @Rollback
    void testIsModelUsed_False() {
        Modele modele = new Modele();
        modele.setNom("Unused");
        modele.setAnnee("2025");
        modele.setFichierHash("hash-fictif");
        modele.setTitre("Titre test");
        modele = modeleRepository.saveAndFlush(modele);

        ResponseEntity<?> response = modeleController.isModelUsed(modele.getId());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("false"));
    }

    @Test
    @Rollback
    void testIsModelUsed_ModelNotFound() {
        ResponseEntity<?> response = modeleController.isModelUsed(999L);
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Modèle"));
    }

    @Test
    @Rollback
    void testUpdateModelConvention_UnauthorizedModification() {
        Modele modele = new Modele();
        modele.setNom("modeleConvention_2023.docx");
        modele.setAnnee("2023");
        modele.setFichierHash("hash-fictif");
        modele.setTitre("Titre original");
        modele = modeleRepository.saveAndFlush(modele);

        ModeleDTO dto = new ModeleDTO(
                modele.getId(),
                "modeleConvention_2025.docx",
                "2025",
                "docx",
                null,
                "Titre de test",
                "Tentative de changement d’année"
        );

        ResponseEntity<Map<String, String>> response = modeleController.updateModelConvention(modele.getId().intValue(), dto);

        assertEquals(400, response.getStatusCode().value());

        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
        assertEquals("La modification de l'année d'un modèle existant n'est pas autorisée.", body.get("error"));
    }

    @Test
    @Rollback
    void testCheckModelNameExists_WhenExists() {
        Modele modele = new Modele();
        modele.setNom("modeleConvention_2026.docx");
        modele.setAnnee("2026");
        modele.setFichierHash("xxx");
        modele.setTitre("exemple");
        modeleRepository.saveAndFlush(modele);

        ResponseEntity<Map<String, Boolean>> response = modeleController.checkModelAnneeExists("2026");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().get("exists"));
    }

    @Test
    @Rollback
    void testUpdateModelFile_Success() throws Exception {
        Modele modele = new Modele();
        modele.setNom("modeleConvention_2030.docx");
        modele.setAnnee("2030");
        modele.setFichierHash("aaa");
        modele.setTitre("titre");
        modele = modeleRepository.saveAndFlush(modele);

        byte[] content = "nouveau contenu".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "fichier.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", content);

        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(modele.getId(), file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fichier remplacé avec succès", response.getBody().get("message"));
    }

    @Test
    @Rollback
    void testDocxVars_ShouldReturnVariableList() throws Exception {
        byte[] content = "dummy docx".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                content
        );

        when(modeleService.extractRawVariables(any())).thenReturn(List.of("VAR1", "VAR2"));

        ResponseEntity<String> response = modeleController.testDocxVars(file);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Variables détectées"));
        assertTrue(response.getBody().contains("VAR1"));
        assertTrue(response.getBody().contains("VAR2"));
    }

    @Test
    @Rollback
    void testGetArchivedModels_ShouldReturnArchivedOnly() {
        Modele archived = new Modele();
        archived.setNom("arch.docx");
        archived.setAnnee("2033");
        archived.setTitre("archivé");
        archived.setFichierHash("hash");
        archived.setArchived(true);
        archived.setArchivedAt(LocalDateTime.now());
        modeleRepository.saveAndFlush(archived);

        Modele actif = new Modele();
        actif.setNom("actif.docx");
        actif.setAnnee("2033");
        actif.setTitre("actif");
        actif.setFichierHash("hash2");
        actif.setArchived(false);
        modeleRepository.saveAndFlush(actif);

        ResponseEntity<List<ModeleDTOForList>> response = modeleController.getArchivedModels();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        ModeleDTOForList archivedDTO = response.getBody().get(0);
        assertTrue(archivedDTO.getNom().contains("arch"));
    }

    @Test
    @Rollback
    void testSearchModeles_WithParams() {
        Modele modele = new Modele();
        modele.setNom("modele.docx");
        modele.setAnnee("2040");
        modele.setTitre("Stage Web");
        modele.setFichierHash("hash");
        modele.setArchived(false);
        modeleRepository.saveAndFlush(modele);

        ResponseEntity<List<ModeleDTOForList>> response = modeleController.searchModeles("2040", "web");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        ModeleDTOForList dto = response.getBody().get(0);
        assertTrue(dto.getTitre().toLowerCase().contains("web"));
    }




}