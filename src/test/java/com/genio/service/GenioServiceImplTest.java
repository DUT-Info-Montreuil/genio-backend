package com.genio.service;

import com.genio.dto.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.model.*;
import com.genio.repository.*;
import com.genio.service.impl.GenioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenioServiceImplTest {

    @InjectMocks
    private GenioServiceImpl genioService;

    @Mock
    private ModeleRepository modeleRepository;
    @Mock
    private HistorisationRepository historisationRepository;
    @Mock
    private EtudiantRepository etudiantRepository;
    @Mock
    private ErrorDetailsRepository errorDetailsRepository;
    @Mock
    private EnseignantRepository enseignantRepository;
    @Mock
    private ConventionRepository conventionRepository;
    @Mock
    private TuteurRepository tuteurRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateConvention_modelNotFound_shouldThrowException() {
        // Arrange
        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(999L);  // ID qui n'existe pas dans le mock

        // Mock l'absence du modèle
        when(modeleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Erreur : modèle introuvable avec l'ID 999", result.getMessageErreur());
        verify(modeleRepository, times(1)).findById(999L);  // Vérifie que la méthode a été appelée une fois
        verify(historisationRepository, times(1)).save(any());  // Vérifie que l'historisation a bien été enregistrée en cas d'échec
    }

    @Test
    void generateConvention_validModel_shouldReturnSuccess() {
        // Arrange
        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(1L);

        Modele modele = new Modele();
        when(modeleRepository.findById(1L)).thenReturn(java.util.Optional.of(modele));

        // Définir les autres champs nécessaires pour ConventionServiceDTO
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com"));
        input.setTuteur(new TuteurDTO("TuteurNom", "TuteurPrenom", "Fonction", "01.23.45.67.89", "tuteur@example.com"));
        input.setOrganisme(new OrganismeDTO("Organisme", "Adresse", "RepNom", "RepQualite", "Service", "01.23.45.67.89", "organisme@example.com", "Lieu"));
        input.setStage(new StageDTO("StageSujet", "2022-01-01", "2022-06-30", "5 mois", 20, 200, "10€", "professionnel"));
        input.setEnseignant(new EnseignantDTO("EnseignantNom", "EnseignantPrenom", "enseignant@example.com"));
        input.setConvention(new ConventionDTO("2022", "CPAM123"));

        // Mock l'enregistrement de l'étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setNom("John");
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Mock l'enregistrement du tuteur
        Tuteur tuteur = new Tuteur();
        tuteur.setNom("TuteurNom");
        when(tuteurRepository.save(any(Tuteur.class))).thenReturn(tuteur);

        // Mock l'enregistrement de l'enseignant
        Enseignant enseignant = new Enseignant();
        enseignant.setNom("EnseignantNom");
        when(enseignantRepository.save(any(Enseignant.class))).thenReturn(enseignant);

        // Mock l'enregistrement de la convention
        Convention convention = new Convention();
        convention.setAnnee("2022");
        when(conventionRepository.save(any(Convention.class))).thenReturn(convention);

        // Act
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.getFichierBinaire());
        verify(modeleRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
        verify(tuteurRepository, times(1)).save(any(Tuteur.class));
        verify(enseignantRepository, times(1)).save(any(Enseignant.class));
        verify(conventionRepository, times(1)).save(any(Convention.class));  // Vérifier que la convention a bien été sauvegardée
    }

    // Vérification des données manquantes
    @Test
    void generateConvention_missingData_shouldReturnValidationError() {
        // Arrange
        ConventionServiceDTO input = new ConventionServiceDTO();
        input.setModeleId(1L);  // ID qui existe dans le mock

        // Mock le modèle trouvé
        Modele modele = new Modele();
        when(modeleRepository.findById(1L)).thenReturn(java.util.Optional.of(modele));

        // Mock l'absence de certains champs dans l'input (par exemple, pas de tuteur)
        input.setEtudiant(new EtudiantDTO("John", "Doe", "H", "2000-01-01", "123 rue Exemple", "01.23.45.67.89", "johndoe@example.com"));
        input.setTuteur(null);  // Pas de tuteur

        // Act
        ConventionBinaireRes result = genioService.generateConvention(input, "DOCX");

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessageErreur().contains("Le champ 'tuteur'"));
        verify(modeleRepository, times(1)).findById(1L);  // Vérifie que la méthode a été appelée une fois
        verify(historisationRepository, times(1)).save(any());  // Vérifie que l'historisation a bien été enregistrée en cas d'échec
    }




}