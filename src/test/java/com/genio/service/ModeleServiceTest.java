package com.genio.service;

import com.genio.dto.outputmodeles.ModeleDTO;
import com.genio.dto.outputmodeles.ModeleDTOForList;
import com.genio.exception.business.*;
import com.genio.mapper.DocxParser;
import com.genio.model.Modele;
import com.genio.repository.ConventionRepository;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeleServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private ConventionRepository conventionRepository;

    @Mock
    private ModeleRepository modeleRepository;

    @Mock
    private DocxParser docxParser;

    @InjectMocks
    private ModeleService modeleService;

    private static final String directoryPath = "src/test/resources/modeles";

    @BeforeEach
    void setup() {
        // Créez le répertoire de test s'il n'existe pas
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Injecter la valeur de directoryPath
        ReflectionTestUtils.setField(modeleService, "directoryPath", directoryPath);
    }

    @Test
    void testCreateModelConvention_Success() throws IOException, MissingVariableException, ModelConventionAlreadyExistsException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.empty());
        when(docxParser.extractVariables(any())).thenReturn(ModeleService.getExpectedVariables());

        ModeleDTO result = modeleService.createModelConvention(file);

        assertNotNull(result);
        assertEquals("modeleConvention_2025.docx", result.getNom());
        verify(modeleRepository, times(1)).save(any());
    }

    @Test
    void testCreateModelConvention_InvalidFormat() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid-name.doc", "application/msword", "content".getBytes());

        InvalidFileFormatException exception = assertThrows(InvalidFileFormatException.class, () ->
                modeleService.createModelConvention(file));

        assertTrue(exception.getMessage().contains("Format invalide"));
    }

    @Test
    void testCreateModelConvention_ModelExists() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.of(new Modele()));

        assertThrows(ModelConventionAlreadyExistsException.class, () -> modeleService.createModelConvention(file));
    }

    @Test
    void testCreateModelConvention_MissingVariables() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.empty());
        when(docxParser.extractVariables(any())).thenReturn(List.of("annee"));

        MissingVariableException exception = assertThrows(MissingVariableException.class, () ->
                modeleService.createModelConvention(file));

        assertTrue(exception.getMessage().contains("Variables manquantes"));
    }

    @Test
    void testDeleteModelConvention_Success() throws ModelConventionNotFoundException, ModelConventionInUseException, DeletionFailedException {
        Modele modele = new Modele();
        modele.setId(1L);

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(conventionRepository.countByModele_Id(1L)).thenReturn(0L);

        assertDoesNotThrow(() -> modeleService.deleteModelConvention(1L));

        verify(modeleRepository, times(1)).delete(modele);
    }

    @Test
    void testDeleteModelConvention_ModelInUse() {
        Modele modele = new Modele();
        modele.setId(1L);

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(conventionRepository.countByModele_Id(1L)).thenReturn(2L);

        assertThrows(ModelConventionInUseException.class, () -> modeleService.deleteModelConvention(1L));
    }

    @Test
    void testDeleteModelConvention_NotFound() {
        when(modeleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ModelConventionNotFoundException.class, () -> modeleService.deleteModelConvention(999L));
    }

    @Test
    void testGetConventionServiceById_Success() throws Exception {
        Modele modele = new Modele();
        modele.setId(1L);
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));

        ModeleDTO result = modeleService.getConventionServiceById(1L);

        assertNotNull(result);
        assertEquals("modeleConvention_2025.docx", result.getNom());
        assertEquals("2025", result.getAnnee());
    }

    @Test
    void testGetConventionServiceById_NotFound() {
        when(modeleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ConventionServiceNotFoundException.class, () ->
                modeleService.getConventionServiceById(999L));
    }

    @Test
    void testGetAllConventionServices_Success() {
        Modele modele = new Modele();
        modele.setId(1L);
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");

        when(modeleRepository.findAll()).thenReturn(List.of(modele));

        List<ModeleDTOForList> result = modeleService.getAllConventionServices();

        assertEquals(1, result.size());
        assertEquals("modeleConvention_2025.docx", result.get(0).getNom());
    }

    @Test
    void testGetAllConventionServices_Empty() {
        when(modeleRepository.findAll()).thenReturn(List.of());

        assertThrows(NoConventionServicesAvailableException.class, () ->
                modeleService.getAllConventionServices());
    }

    @Test
    void testIsModelInUse_True() throws Exception {
        Modele modele = new Modele();
        modele.setId(1L);
        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(conventionRepository.countByModele_Id(1L)).thenReturn(2L);

        boolean result = modeleService.isModelInUse(1L);
        assertTrue(result);
    }

    @Test
    void testUpdateModelConvention_Success() throws Exception {
        Modele modele = new Modele();
        modele.setId(1L);
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");

        ModeleDTO dto = new ModeleDTO(1L, "modeleConvention_2026.docx", "2025", "docx", "n/a");

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(modeleRepository.findFirstByNom(dto.getNom())).thenReturn(Optional.empty());

        modeleService.updateModelConvention(1, dto);

        verify(modeleRepository).save(any());
    }

    @Test
    void testInsertModele_ShouldThrowException_WhenFileIsEmpty() {
        File emptyFile = new File(directoryPath + "/empty-model.docx");
        try {
            emptyFile.createNewFile();
        } catch (IOException e) {
            fail("File creation failed");
        }

        Exception exception = assertThrows(EmptyFileException.class, () -> {
            modeleService.insertModele(emptyFile, "2025");
        });

        assertTrue(exception.getMessage().contains("Le fichier empty-model.docx est vide"));
    }
}