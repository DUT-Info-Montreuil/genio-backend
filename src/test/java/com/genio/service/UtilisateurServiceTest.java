package com.genio.service;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.UtilisateurService;
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
        dto.setUsername("testuser");
        dto.setMotDePasse("password");

        Utilisateur utilisateurSaved = Utilisateur.builder()
                .nom("Test")
                .prenom("User")
                .username("testuser")
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
        dto.setUsername("newuser");
        dto.setMotDePasse("newpassword");

        Optional<Utilisateur> updatedUtilisateur = utilisateurService.modifierUtilisateur(1L, dto);

        assertTrue(updatedUtilisateur.isPresent());
        assertEquals("NouveauNom", updatedUtilisateur.get().getNom());
    }
}