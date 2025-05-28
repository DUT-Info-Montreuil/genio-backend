package com.genio.service;

import com.genio.exception.business.CompteInactifException;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UtilisateurRepository utilisateurRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        customUserDetailsService = new CustomUserDetailsService(utilisateurRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserIsActive() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setMotDePasse("hashed");
        utilisateur.setRole("ADMIN");
        utilisateur.setActif(true);

        when(utilisateurRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(utilisateur));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashed", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(utilisateurRepository.findByEmail("inconnu@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("inconnu@example.com"));
    }

    @Test
    void loadUserByUsername_shouldThrowCompteInactifException_whenUserIsInactive() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("inactive@example.com");
        utilisateur.setMotDePasse("pass");
        utilisateur.setRole("UTILISATEUR");
        utilisateur.setActif(false);

        when(utilisateurRepository.findByEmail("inactive@example.com"))
                .thenReturn(Optional.of(utilisateur));

        assertThrows(CompteInactifException.class, () ->
                customUserDetailsService.loadUserByUsername("inactive@example.com"));
    }
}