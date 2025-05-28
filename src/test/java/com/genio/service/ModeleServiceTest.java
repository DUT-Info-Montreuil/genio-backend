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
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ReflectionTestUtils.setField(modeleService, "directoryPath", directoryPath);
    }

    @Test
    void testCreateModelConvention_Success() throws IOException, MissingVariableException, ModelConventionAlreadyExistsException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.empty());
        when(docxParser.extractVariables(any())).thenReturn(ModeleService.getExpectedVariables());

        ModeleDTO result = modeleService.createModelConvention(file, "2025", "Titre test");

        assertNotNull(result);
        assertEquals("modeleConvention_2025.docx", result.getNom());
        verify(modeleRepository, times(1)).save(any());
    }

    @Test
    void testCreateModelConvention_InvalidFormat() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid-name.doc", "application/msword", "content".getBytes());

        InvalidFileFormatException exception = assertThrows(InvalidFileFormatException.class, () ->
                modeleService.createModelConvention(file, "2025", "Titre Test"));

        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().toLowerCase().contains("format"));
    }

    @Test
    void testCreateModelConvention_ModelExists() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.of(new Modele()));

        assertThrows(ModelConventionAlreadyExistsException.class, () -> modeleService.createModelConvention(file, "2025", "Titre Test"));
    }

    @Test
    void testCreateModelConvention_MissingVariables() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "modeleConvention_2025.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        when(modeleRepository.findFirstByNom(any())).thenReturn(Optional.empty());
        when(docxParser.extractVariables(any())).thenReturn(List.of("annee"));

        MissingVariableException exception = assertThrows(MissingVariableException.class, () ->
                modeleService.createModelConvention(file, "2025", "Titre Test"));

        assertTrue(exception.getMessage().toLowerCase().contains("erreurs de variables"));
    }

    @Test
    void testDeleteModelConvention_Success() throws ModelConventionNotFoundException, ModelConventionInUseException, DeletionFailedException {
        Modele modele = new Modele();
        modele.setId(1L);

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(conventionRepository.countByModele_Id(1L)).thenReturn(0L);

        assertDoesNotThrow(() -> modeleService.archiveModelConvention(1L));

        verify(modeleRepository, times(1)).save(modele);
    }

    @Test
    void testDeleteModelConvention_ModelInUse() {
        Modele modele = new Modele();
        modele.setId(1L);

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));
        when(conventionRepository.countByModele_Id(1L)).thenReturn(2L);

        assertThrows(ModelConventionInUseException.class, () -> modeleService.archiveModelConvention(1L));
    }

    @Test
    void testDeleteModelConvention_NotFound() {
        when(modeleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ModelConventionNotFoundException.class, () -> modeleService.archiveModelConvention(999L));
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



    @Test
    void testUpdateModelConvention_Success() throws Exception {
        Modele modele = new Modele();
        modele.setId(1L);
        modele.setNom("modeleConvention_2025.docx");
        modele.setAnnee("2025");

        ModeleDTO dto = new ModeleDTO(1L, "modeleConvention_2026.docx", "2025", "docx", "n/a", "Titre test", "Modif description");

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));

        modeleService.updateModelConvention(1, dto);
        verify(modeleRepository).save(any());
    }

    @Test
    void testInsertModeleFromDirectory_InvalidFilename_ThrowsException() throws IOException {
        File dir = new File(directoryPath);
        for (File f : dir.listFiles()) f.delete();

        File invalidFile = new File(directoryPath + "/invalide.docx");
        Files.write(invalidFile.toPath(), "dummy content".getBytes());

        ModeleService spyService = spy(modeleService);
        ReflectionTestUtils.setField(spyService, "directoryPath", directoryPath);

        InvalidFileFormatException exception = assertThrows(
                InvalidFileFormatException.class,
                spyService::insertModeleFromDirectory
        );

        assertTrue(exception.getMessage().contains("Format invalide"));
    }

    @Test
    void testInsertModeleFromDirectory_ValidFile_CallsInsert() throws IOException {
        File dir = new File(directoryPath);
        for (File file : dir.listFiles()) {
            file.delete();
        }

        File validFile = new File(directoryPath + "/modeleConvention_2025.docx");
        Files.write(validFile.toPath(), "dummy content".getBytes());

        ModeleService spyService = spy(modeleService);
        doNothing().when(spyService).insertModele(any(File.class), eq("2025"));
        ReflectionTestUtils.setField(spyService, "directoryPath", directoryPath);

        spyService.insertModeleFromDirectory();

        verify(spyService, times(1)).insertModele(any(File.class), eq("2025"));
    }

    @Test
    void testInsertModele_MissingYear_ThrowsException() throws IOException {
        File file = new File(directoryPath + "/modeleSansAnnee.docx");
        Files.writeString(file.toPath(), "Contenu");

        Exception exception = assertThrows(InvalidFileFormatException.class, () ->
                modeleService.insertModele(file, ""));

        assertTrue(exception.getMessage().contains("doit contenir une année"));
    }

    @Test
    void testInsertModele_DataSourceNull_ThrowsException() throws IOException {
        File file = new File(directoryPath + "/modeleConvention_2025.docx");
        Files.writeString(file.toPath(), "Contenu");

        ModeleService modeleServiceNullDS = new ModeleService(null, conventionRepository, modeleRepository, docxParser);
        ReflectionTestUtils.setField(modeleServiceNullDS, "directoryPath", directoryPath);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                modeleServiceNullDS.insertModele(file, "2025"));

        assertTrue(exception.getMessage().contains("DataSource is not initialized"));
    }

    @Test
    void testInsertModele_Success() throws Exception {
        File validFile = new File(directoryPath + "/modeleConvention_2025.docx");
        Files.write(validFile.toPath(), "Contenu du modèle".getBytes());

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        modeleService.insertModele(validFile, "2025");

        verify(mockConnection, times(1)).prepareStatement("INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)");
        verify(mockPreparedStatement, times(1)).setString(eq(1), eq("modeleConvention_2025.docx"));
        verify(mockPreparedStatement, times(1)).setString(eq(2), eq("2025"));
        verify(mockPreparedStatement, times(1)).setBytes(eq(3), any());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }


}