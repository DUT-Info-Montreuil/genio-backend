package com.genio.service.impl;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreerUtilisateur() {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Test");
        dto.setPrenom("User");
        dto.setEmail("testuser@genio.com");
        dto.setMotDePasse("password");

        Utilisateur utilisateurSaved = Utilisateur.builder()
                .nom("Test")
                .prenom("User")
                .email("testuser@genio.com")
                .motDePasse("hashedPassword")
                .role("UTILISATEUR")
                .actif(false)
                .build();

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateurSaved);

        Utilisateur utilisateur = utilisateurService.creerUtilisateur(dto);

        assertNotNull(utilisateur);
        assertEquals("Test", utilisateur.getNom());
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testGetAllUtilisateurs() {
        when(utilisateurRepository.findAll()).thenReturn(List.of(new Utilisateur(), new Utilisateur()));

        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();

        assertEquals(2, utilisateurs.size());
        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void testSupprimerUtilisateur() {
        utilisateurService.supprimerUtilisateur(1L);

        verify(utilisateurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testModifierUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("NouveauNom");
        dto.setPrenom("NouveauPrenom");
        dto.setEmail("testuser@genio.com");
        dto.setMotDePasse("newpassword");

        Optional<Utilisateur> updatedUtilisateur = utilisateurService.modifierUtilisateur(1L, dto);

        assertTrue(updatedUtilisateur.isPresent());
        assertEquals("NouveauNom", updatedUtilisateur.get().getNom());
    }

    @Test
    void testModifierRoleEtActivation() {
        Long id = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setRole("UTILISATEUR");
        utilisateur.setActif(false);

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Utilisateur result = utilisateurService.modifierRoleEtActivation(id, "ADMIN", true);

        assertNotNull(result);
        assertEquals("ADMIN", result.getRole());
        assertTrue(result.isActif());
        verify(utilisateurRepository).findById(id);
        verify(utilisateurRepository).save(utilisateur);
    }

    @Test
    void testModifierRoleEtStatut() {
        Long id = 2L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setRole("UTILISATEUR");
        utilisateur.setActif(false);

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Utilisateur> result = utilisateurService.modifierRoleEtStatut(id, "GESTIONNAIRE", true);

        assertTrue(result.isPresent());
        assertEquals("GESTIONNAIRE", result.get().getRole());
        assertTrue(result.get().isActif());
        verify(utilisateurRepository).findById(id);
        verify(utilisateurRepository).save(utilisateur);
    }

    @Test
    void testGetByEmail() {
        String email = "test@genio.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

        Optional<Utilisateur> result = utilisateurService.getByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(utilisateurRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUtilisateursNonActifs() {
        Utilisateur actif1 = new Utilisateur();
        actif1.setRole("UTILISATEUR");
        actif1.setActif(true);

        Utilisateur inactif1 = new Utilisateur();
        inactif1.setRole("UTILISATEUR");
        inactif1.setActif(false);

        Utilisateur inactif2 = new Utilisateur();
        inactif2.setRole("ADMIN");
        inactif2.setActif(false);

        Utilisateur exclu1 = new Utilisateur();
        exclu1.setRole("GESTIONNAIRE");
        exclu1.setActif(false);

        when(utilisateurRepository.findAll()).thenReturn(List.of(actif1, inactif1, inactif2, exclu1));

        List<Utilisateur> result = utilisateurService.getUtilisateursNonActifs();

        assertEquals(2, result.size());
        assertTrue(result.contains(inactif1));
        assertTrue(result.contains(inactif2));
        assertFalse(result.contains(actif1));
        assertFalse(result.contains(exclu1));
    }

    @Test
    void testModifierRoleEtActivation_shouldUpdateRoleAndActivation() {
        Long id = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setRole("UTILISATEUR");
        utilisateur.setActif(false);

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Utilisateur result = utilisateurService.modifierRoleEtActivation(id, "ADMIN", true);

        assertEquals("ADMIN", result.getRole());
        assertTrue(result.isActif());
        assertNotNull(result.getUpdatedAt());
        verify(utilisateurRepository, times(1)).save(utilisateur);
    }

    @Test
    void testModifierRoleEtStatut_shouldUpdateOnlyNonNullFields() {
        Long id = 2L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setRole("UTILISATEUR");
        utilisateur.setActif(false);

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Utilisateur> result = utilisateurService.modifierRoleEtStatut(id, "ADMIN", null);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getRole());
        assertFalse(result.get().isActif());
        assertNotNull(result.get().getUpdatedAt());
        verify(utilisateurRepository, times(1)).save(utilisateur);
    }

    @Test
    void testGetByEmail_shouldReturnUtilisateurIfExists() {
        String email = "testuser@genio.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

        Optional<Utilisateur> result = utilisateurService.getByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(utilisateurRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUtilisateursNonActifs_shouldReturnFilteredList() {
        Utilisateur actifAdmin = Utilisateur.builder()
                .role("ADMIN")
                .actif(true)
                .build();

        Utilisateur inactifUtilisateur = Utilisateur.builder()
                .role("UTILISATEUR")
                .actif(false)
                .build();

        Utilisateur inactifConsultant = Utilisateur.builder()
                .role("CONSULTANT")
                .actif(false)
                .build();

        Utilisateur inactifAutre = Utilisateur.builder()
                .role("STAGIAIRE")
                .actif(false)
                .build();

        when(utilisateurRepository.findAll()).thenReturn(List.of(
                actifAdmin,
                inactifUtilisateur,
                inactifConsultant,
                inactifAutre
        ));

        List<Utilisateur> result = utilisateurService.getUtilisateursNonActifs();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> !u.isActif()));
        assertTrue(result.stream().noneMatch(u -> u.getRole().equals("GESTIONNAIRE") || u.getRole().equals("CONSULTANT") || u.getRole().equals("EXPLOITANT")));

        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void testGetUtilisateursNonActifs_shouldReturnEmptyListWhenNoUsersExist() {
        when(utilisateurRepository.findAll()).thenReturn(List.of());

        List<Utilisateur> result = utilisateurService.getUtilisateursNonActifs();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "La liste des utilisateurs non actifs doit être vide");

        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void testGetByEmail_whenEmailExists_shouldReturnUtilisateur() {
        String email = "test@genio.com";
        Utilisateur utilisateur = Utilisateur.builder()
                .id(1L)
                .nom("Test")
                .email(email)
                .build();

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

        Optional<Utilisateur> result = utilisateurService.getByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(utilisateurRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetByEmail_whenEmailDoesNotExist_shouldReturnEmptyOptional() {
        String email = "inconnu@genio.com";

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<Utilisateur> result = utilisateurService.getByEmail(email);

        assertFalse(result.isPresent());
        verify(utilisateurRepository, times(1)).findByEmail(email);
    }

    @Test
    void testModifierRoleEtActivation_shouldUpdateRoleAndActif() {
        Long userId = 1L;
        String nouveauRole = "ADMIN";
        boolean actif = true;

        Utilisateur utilisateur = Utilisateur.builder()
                .id(userId)
                .nom("Test")
                .role("UTILISATEUR")
                .actif(false)
                .build();

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Utilisateur result = utilisateurService.modifierRoleEtActivation(userId, nouveauRole, actif);

        assertEquals(nouveauRole, result.getRole());
        assertTrue(result.isActif());
        assertNotNull(result.getUpdatedAt());
        verify(utilisateurRepository, times(1)).findById(userId);
        verify(utilisateurRepository, times(1)).save(utilisateur);
    }

    @Test
    void testModifierRoleEtStatut_shouldUpdateRoleAndActif() {
        Long userId = 2L;
        String newRole = "GESTIONNAIRE";
        Boolean actif = false;

        Utilisateur utilisateur = Utilisateur.builder()
                .id(userId)
                .role("UTILISATEUR")
                .actif(true)
                .build();

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Utilisateur> result = utilisateurService.modifierRoleEtStatut(userId, newRole, actif);

        assertTrue(result.isPresent());
        assertEquals(newRole, result.get().getRole());
        assertFalse(result.get().isActif());
        assertNotNull(result.get().getUpdatedAt());
        verify(utilisateurRepository).findById(userId);
        verify(utilisateurRepository).save(utilisateur);
    }

    @Test
    void testGetUtilisateursNonActifs_shouldReturnOnlyNonActifsExcludingSpecificRoles() {
        Utilisateur actifUtilisateur = Utilisateur.builder()
                .email("actif@genio.com")
                .role("UTILISATEUR")
                .actif(true)
                .build();

        Utilisateur inactifIgnore = Utilisateur.builder()
                .email("ignore@genio.com")
                .role("GESTIONNAIRE")
                .actif(false)
                .build();

        Utilisateur inactifCorrect = Utilisateur.builder()
                .email("correct@genio.com")
                .role("UTILISATEUR")
                .actif(false)
                .build();

        when(utilisateurRepository.findAll()).thenReturn(List.of(actifUtilisateur, inactifIgnore, inactifCorrect));

        List<Utilisateur> result = utilisateurService.getUtilisateursNonActifs();

        assertEquals(1, result.size());
        assertEquals("correct@genio.com", result.get(0).getEmail());
        verify(utilisateurRepository).findAll();
    }
}