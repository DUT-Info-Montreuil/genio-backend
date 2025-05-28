package com.genio.controller;

import com.genio.dto.LoginResponse;
import com.genio.exception.business.CompteInactifException;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.MailService;
import com.genio.service.impl.TokenService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthenticationManager authenticationManager;
    private UtilisateurRepository utilisateurRepository;
    private TokenService tokenService;
    private MailService mailService;


    @Mock
    private HttpSession session;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        tokenService = mock(TokenService.class);
        mailService = mock(MailService.class);

        authController = new AuthController(authenticationManager, utilisateurRepository, tokenService, mailService);
    }

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturn200_whenAuthenticationSucceeds() {
        // Données d’entrée
        String email = "test@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);

        // Simuler une authentification réussie
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Exécution
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Connexion réussie", response.getBody().getMessage());
        verify(session).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
    }

    @Test
    void login_shouldReturn401_whenBadCredentials() {
        // Données d’entrée
        String email = "wrong@example.com";
        String password = "wrongpass";
        HttpSession session = mock(HttpSession.class);

        // Simule une authentification échouée avec BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Exécution
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        // Vérification
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Email ou mot de passe incorrect.", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn403_whenCompteInactif() {
        // Données d’entrée
        String email = "inactive@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);

        // Simule une InternalAuthenticationServiceException avec cause CompteInactifException
        CompteInactifException inactiveException = new CompteInactifException("Compte désactivé");
        InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("Auth fail", inactiveException);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(exception);

        // Exécution
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        // Vérification
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Compte désactivé", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn500_whenInternalAuthServiceFailsWithoutCause() {
        // Données d’entrée
        String email = "error@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);

        // Simule InternalAuthenticationServiceException sans cause
        InternalAuthenticationServiceException exception =
                new InternalAuthenticationServiceException("Unexpected failure");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(exception);

        // Exécution
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        // Vérification
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erreur interne d'authentification.", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn500_whenUnexpectedExceptionThrown() {
        // Given
        String email = "test@example.com";
        String password = "password";

        // Simule une exception générique inattendue
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Unexpected"));

        // When
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erreur inattendue lors de l'authentification.", response.getBody().getMessage());
    }

    @Test
    void motDePasseOublie_shouldReturn400_whenEmailIsMissing() {
        // Given
        Map<String, String> request = Collections.emptyMap(); // Aucun email fourni

        // When
        ResponseEntity<Map<String, String>> response = authController.motDePasseOublie(request);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email requis.", response.getBody().get("message"));
    }

    @Test
    void motDePasseOublie_shouldSendEmail_whenEmailIsValid() {
        // Given
        String email = "test@example.com";
        Map<String, String> request = Map.of("email", email);

        Utilisateur mockUser = new Utilisateur();
        mockUser.setEmail(email);

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(tokenService.generateResetToken(email)).thenReturn("dummy-token");

        // When
        ResponseEntity<Map<String, String>> response = authController.motDePasseOublie(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Si cet email est enregistré, un e-mail a été envoyé.", response.getBody().get("message"));
        verify(mailService).sendResetPasswordEmail(eq(email), eq("dummy-token"));
    }

    @Test
    void resetPassword_shouldReturn400_whenTokenOrPasswordIsMissing() {
        // Cas 1 : token manquant
        Map<String, String> requestSansToken = Map.of("nouveauMotDePasse", "newpass");
        ResponseEntity<Map<String, String>> response1 = authController.resetPassword(requestSansToken);
        assertEquals(400, response1.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response1.getBody().get("message"));

        // Cas 2 : mot de passe manquant
        Map<String, String> requestSansPassword = Map.of("token", "abc123");
        ResponseEntity<Map<String, String>> response2 = authController.resetPassword(requestSansPassword);
        assertEquals(400, response2.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response2.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn200_whenResetIsSuccessful() {
        // Préparation
        String token = "valid-token";
        String nouveauMotDePasse = "NewPassword123!";
        Map<String, String> request = Map.of(
                "token", token,
                "nouveauMotDePasse", nouveauMotDePasse
        );

        when(tokenService.resetPassword(token, nouveauMotDePasse)).thenReturn(true);

        // Exécution
        ResponseEntity<Map<String, String>> response = authController.resetPassword(request);

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mot de passe réinitialisé avec succès.", response.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn400_whenTokenIsInvalid() {
        // Préparation
        String token = "invalid-or-expired-token";
        String nouveauMotDePasse = "SomeNewPassword!";
        Map<String, String> request = Map.of(
                "token", token,
                "nouveauMotDePasse", nouveauMotDePasse
        );

        when(tokenService.resetPassword(token, nouveauMotDePasse)).thenReturn(false);

        // Exécution
        ResponseEntity<Map<String, String>> response = authController.resetPassword(request);

        // Vérification
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Token invalide ou expiré.", response.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn400_whenFieldsAreMissing() {
        // Cas 1 : aucun champ
        Map<String, String> emptyRequest = Map.of();

        ResponseEntity<Map<String, String>> response1 = authController.resetPassword(emptyRequest);
        assertEquals(400, response1.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response1.getBody().get("message"));

        // Cas 2 : token vide
        Map<String, String> missingToken = Map.of("token", "", "nouveauMotDePasse", "password123");
        ResponseEntity<Map<String, String>> response2 = authController.resetPassword(missingToken);
        assertEquals(400, response2.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response2.getBody().get("message"));

        // Cas 3 : mot de passe vide
        Map<String, String> missingPassword = Map.of("token", "validToken", "nouveauMotDePasse", "");
        ResponseEntity<Map<String, String>> response3 = authController.resetPassword(missingPassword);
        assertEquals(400, response3.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response3.getBody().get("message"));
    }

}