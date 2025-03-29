package com.genio.service;

import com.genio.controller.ModeleController;
import com.genio.dto.output.ModeleDTOForList;
import com.genio.exception.business.*;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ModeleControllerTest {

    @Autowired
    private ModeleController modeleController;

    @Autowired
    private ModeleService modeleService;

    @Autowired
    private ModeleRepository modeleRepository;

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

        ResponseEntity<?> response = modeleController.createModelConvention("modeleConvention_2025.docx", file);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("ModelConvention ajouté avec succès"));
    }

    @Test
    @Rollback
    void testCreateModelConvention_ModelAlreadyExists() throws IOException {
        byte[] fileContent = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile("file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", fileContent);

        Modele modele = new Modele();
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");
        modeleRepository.save(modele);

        ResponseEntity<?> response = modeleController.createModelConvention("Convention Stage 2025", file);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Un modèle avec ce nom existe déjà"));
    }

    @Test
    @Rollback
    void testCreateModelConvention_InvalidFileFormat() throws IOException {
        byte[] fileContent = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile("file", "modeleConvention.txt", "text/plain", fileContent);

        ResponseEntity<?> response = modeleController.createModelConvention("Convention Stage 2025", file);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Format non supporté, uniquement .docx accepté"));
    }

    @Test
    @Rollback
    void testGetAllModelConvention() {
        Modele modele = new Modele();
        modele.setNom("Modele Test");
        modele.setAnnee("2025");
        modeleRepository.save(modele);

        ResponseEntity<?> response = modeleController.getAllModelConvention();

        assertEquals(200, response.getStatusCodeValue());
        List<ModeleDTOForList> modeles = (List<ModeleDTOForList>) response.getBody();
        assertFalse(modeles.isEmpty());
    }

    @Test
    @Rollback
    void testGetModelConventionById_NotFound() {
        ResponseEntity<?> response = modeleController.getModelConventionById(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Modèle introuvable"));
    }

    @Test
    @Rollback
    void testDeleteModelConvention_Success() {
        Modele modele = new Modele();
        modele.setNom("Modele à supprimer");
        modele.setAnnee("2025");
        modeleRepository.save(modele);

        ResponseEntity<?> response = modeleController.deleteModelConvention(modele.getId());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("ModelConvention supprimé avec succès"));
    }

    @Test
    @Rollback
    void testDeleteModelConvention_NotFound() {
        ResponseEntity<?> response = modeleController.deleteModelConvention(999L);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Modèle introuvable"));
    }

    @Test
    @Rollback
    void testDeleteModelConvention_ModelInUse() {
        Modele modele = new Modele();
        modele.setNom("Modele en utilisation");
        modele.setAnnee("2025");
        modeleRepository.save(modele);

        ResponseEntity<?> response = modeleController.deleteModelConvention(modele.getId());

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Le modèle est toujours utilisé et ne peut pas être supprimé"));
    }
}