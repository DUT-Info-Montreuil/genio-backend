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

package com.genio.service.impl;

import com.genio.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import com.genio.model.Utilisateur;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        tokenService = new TokenService(utilisateurRepository, encoder);
    }

    @Test
    void generateResetToken_shouldReturnNonEmptyToken_andStoreIt() {
        String email = "user@example.com";
        String token = tokenService.generateResetToken(email);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void resetPassword_shouldReturnFalse_whenUserNotFound() {
        String email = "unknown@example.com";
        String token = tokenService.generateResetToken(email);
        UtilisateurRepository repoMock = mock(UtilisateurRepository.class);
        TokenService localTokenService = new TokenService(repoMock, new BCryptPasswordEncoder());
        String tokenCopy = token;
        localTokenService.generateResetToken(email);

        boolean result = localTokenService.resetPassword(tokenCopy, "newPass");

        assertFalse(result);
    }

    @Test
    void resetPassword_shouldReturnFalse_whenTokenInvalid() {
        boolean result = tokenService.resetPassword("invalid-token", "newPassword");
        assertFalse(result);
    }

    @Test
    void resetPassword_shouldUpdatePasswordAndRemoveToken_whenValid() {

        String email = "user@example.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);

        UtilisateurRepository repoMock = mock(UtilisateurRepository.class);
        when(repoMock.findByEmail(email)).thenReturn(Optional.of(utilisateur));
        when(repoMock.save(any(Utilisateur.class))).thenAnswer(i -> i.getArgument(0));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TokenService service = new TokenService(repoMock, encoder);

        String token = service.generateResetToken(email);

        boolean result = service.resetPassword(token, "newPassword");

        assertTrue(result);
        verify(repoMock, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void resetPassword_shouldRemoveTokenAfterUse() {

        String email = "user@example.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);

        UtilisateurRepository repoMock = mock(UtilisateurRepository.class);
        when(repoMock.findByEmail(email)).thenReturn(Optional.of(utilisateur));
        when(repoMock.save(any(Utilisateur.class))).thenReturn(utilisateur);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TokenService localService = new TokenService(repoMock, encoder);
        String token = localService.generateResetToken(email);

        boolean firstTry = localService.resetPassword(token, "pass1");

        boolean secondTry = localService.resetPassword(token, "pass2");

        assertTrue(firstTry);
        assertFalse(secondTry);
    }

    @Test
    void resetPassword_shouldReturnFalse_whenEmailNotFoundInRepository() {
        String email = "notfound@example.com";
        UtilisateurRepository repoMock = mock(UtilisateurRepository.class);
        when(repoMock.findByEmail(email)).thenReturn(Optional.empty()); 

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TokenService service = new TokenService(repoMock, encoder);

        String token = service.generateResetToken(email); 

        boolean result = service.resetPassword(token, "newPassword");

        assertFalse(result); 
    }


}