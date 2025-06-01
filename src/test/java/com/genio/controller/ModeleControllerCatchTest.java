package com.genio.controller;

import com.genio.dto.outputmodeles.ModeleDTO;
import com.genio.exception.business.ModelConventionAlreadyExistsException;
import com.genio.exception.business.ModelConventionNotFoundException;
import com.genio.exception.business.NoConventionServicesAvailableException;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ModeleControllerCatchTest {
    @MockBean
    private ModeleService modeleService;

    @Autowired
    private ModeleController modeleController;

    @MockBean
    private JavaMailSender javaMailSender;

    @Spy
    private Logger spyLogger = LoggerFactory.getLogger(ModeleController.class);

    @Test
    void testCreateModelConvention_CatchModelAlreadyExists() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "modeleConvention_2025.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleService.createModelConvention(any(), anyString(), anyString()))
                .thenThrow(new ModelConventionAlreadyExistsException("Fichier déjà utilisé"));

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre");

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Fichier déjà utilisé"));
    }

    @Test
    void testGetAllModelConvention_CatchNoConventionServicesAvailable() throws Exception {
        when(modeleService.getAllConventionServices())
                .thenThrow(new NoConventionServicesAvailableException("Aucun modèle"));

        ResponseEntity<?> response = modeleController.getAllModelConvention();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof java.util.List);
        assertTrue(((java.util.List<?>) response.getBody()).isEmpty());
    }

    @Test
    void testCreateModelConvention_CatchDatabaseInsertionException() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "modeleConvention_2025.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleService.createModelConvention(any(), anyString(), anyString()))
                .thenThrow(new com.genio.exception.business.DatabaseInsertionException("Erreur base de données", null));

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre");

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Erreur lors de l'enregistrement du modèle"));
    }

    @Test
    void testCreateModelConvention_CatchIOException() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "modeleConvention_2025.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleService.createModelConvention(any(), anyString(), anyString()))
                .thenThrow(new IOException("Erreur IO"));

        ResponseEntity<?> response = modeleController.createModelConvention(file, "2025", "Titre");

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Erreur lors de l'enregistrement du modèle"));
    }

    @Test
    void testUpdateModelConvention_CatchGenericException() throws Exception {
        ModeleDTO dto = new ModeleDTO(
                1L,
                "modeleConvention_2025.docx",
                "2025",
                "docx",
                null,
                "Titre test",
                null
        );

        doThrow(new RuntimeException("Erreur inattendue"))
                .when(modeleService).updateModelConvention(anyInt(), any(ModeleDTO.class));

        ResponseEntity<Map<String, String>> response = modeleController.updateModelConvention(1, dto);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Erreur interne du serveur", response.getBody().get("error"));
    }

    @Test
    void testUpdateModelConvention_Success() throws Exception {
        ModeleDTO dto = new ModeleDTO(
                1L,
                "modeleConvention_2025.docx",
                "2025",
                "docx",
                null,
                "Titre test",
                null
        );

        doNothing().when(modeleService).updateModelConvention(anyInt(), any(ModeleDTO.class));

        ResponseEntity<Map<String, String>> response = modeleController.updateModelConvention(1, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("ModelConvention mis à jour avec succès !", response.getBody().get("message"));
    }


    @Test
    void testDocxVars_ReturnsVariables() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "dummy content".getBytes()
        );

        when(modeleService.extractRawVariables(any())).thenReturn(List.of("VAR1", "VAR2"));

        ResponseEntity<String> response = modeleController.testDocxVars(file);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Variables détectées"));
        assertTrue(response.getBody().contains("VAR1"));
        assertTrue(response.getBody().contains("VAR2"));
    }

    @Test
    void testDocxVars_Returns400WhenEmptyOrNull() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                new byte[0]
        );

        // Simule que le service retourne null
        when(modeleService.extractRawVariables(any())).thenReturn(null);

        ResponseEntity<String> responseNull = modeleController.testDocxVars(file);
        assertEquals(400, responseNull.getStatusCodeValue());
        assertTrue(responseNull.getBody().contains("Aucun contenu exploitable détecté"));

        // Simule que le service retourne une liste vide
        when(modeleService.extractRawVariables(any())).thenReturn(List.of());

        ResponseEntity<String> responseEmpty = modeleController.testDocxVars(file);
        assertEquals(400, responseEmpty.getStatusCodeValue());
        assertTrue(responseEmpty.getBody().contains("Aucun contenu exploitable détecté"));
    }

    @Test
    void testDocxVars_CatchException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "error.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "dummy content".getBytes()
        );

        when(modeleService.extractRawVariables(any())).thenThrow(new RuntimeException("Erreur interne"));

        ResponseEntity<String> response = modeleController.testDocxVars(file);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Erreur : Erreur interne"));
    }

    @Test
    void testUpdateModelFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "newFile.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        // Pas d'exception levée => succès
        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(1L, file);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Fichier remplacé avec succès", response.getBody().get("message"));
    }

    @Test
    void testUpdateModelFile_CatchModelConventionAlreadyExistsException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "existingFile.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        doThrow(new ModelConventionAlreadyExistsException("Fichier déjà utilisé"))
                .when(modeleService).replaceModelFile(anyLong(), any());

        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(1L, file);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Ce fichier est déjà utilisé dans un autre modèle actif.", response.getBody().get("error"));
    }

    @Test
    void testUpdateModelFile_CatchModelConventionNotFoundException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "someFile.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        doThrow(new ModelConventionNotFoundException("Modèle introuvable"))
                .when(modeleService).replaceModelFile(anyLong(), any());

        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(1L, file);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Modèle introuvable.", response.getBody().get("error"));
    }

    @Test
    void testUpdateModelFile_CatchIOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "fileIOError.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        doThrow(new IOException("Erreur IO"))
                .when(modeleService).replaceModelFile(anyLong(), any());

        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(1L, file);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erreur lors de la lecture du fichier.", response.getBody().get("error"));
    }

    @Test
    void testUpdateModelFile_CatchGenericException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "fileGenericError.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        doThrow(new RuntimeException("Erreur inattendue"))
                .when(modeleService).replaceModelFile(anyLong(), any());

        ResponseEntity<Map<String, String>> response = modeleController.updateModelFile(1L, file);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erreur serveur inattendue.", response.getBody().get("error"));
    }



}
