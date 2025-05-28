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

class AuthControllerTest {

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
        String email = "test@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Connexion réussie", response.getBody().getMessage());
        verify(session).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
    }

    @Test
    void login_shouldReturn401_whenBadCredentials() {
        String email = "wrong@example.com";
        String password = "wrongpass";
        HttpSession session = mock(HttpSession.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Email ou mot de passe incorrect.", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn403_whenCompteInactif() {
        String email = "inactive@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);

        CompteInactifException inactiveException = new CompteInactifException("Compte désactivé");
        InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("Auth fail", inactiveException);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(exception);

        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Compte désactivé", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn500_whenInternalAuthServiceFailsWithoutCause() {
        String email = "error@example.com";
        String password = "password";
        HttpSession session = mock(HttpSession.class);
        
        InternalAuthenticationServiceException exception =
                new InternalAuthenticationServiceException("Unexpected failure");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(exception);

        ResponseEntity<LoginResponse> response = authController.login(email, password, session);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erreur interne d'authentification.", response.getBody().getMessage());
    }

    @Test
    void login_shouldReturn500_whenUnexpectedExceptionThrown() {
        String email = "test@example.com";
        String password = "password";

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Unexpected"));
        
        ResponseEntity<LoginResponse> response = authController.login(email, password, session);
 
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erreur inattendue lors de l'authentification.", response.getBody().getMessage());
    }

    @Test
    void motDePasseOublie_shouldReturn400_whenEmailIsMissing() {
  
        Map<String, String> request = Collections.emptyMap();
        
        ResponseEntity<Map<String, String>> response = authController.motDePasseOublie(request);
 
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email requis.", response.getBody().get("message"));
    }

    @Test
    void motDePasseOublie_shouldSendEmail_whenEmailIsValid() {
  
        String email = "test@example.com";
        Map<String, String> request = Map.of("email", email);

        Utilisateur mockUser = new Utilisateur();
        mockUser.setEmail(email);

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(tokenService.generateResetToken(email)).thenReturn("dummy-token");

        
        ResponseEntity<Map<String, String>> response = authController.motDePasseOublie(request);
 
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Si cet email est enregistré, un e-mail a été envoyé.", response.getBody().get("message"));
        verify(mailService).sendResetPasswordEmail(email, "dummy-token");
    }

    @Test
    void resetPassword_shouldReturn400_whenTokenOrPasswordIsMissing() {
        Map<String, String> requestSansToken = Map.of("nouveauMotDePasse", "newpass");
        ResponseEntity<Map<String, String>> response1 = authController.resetPassword(requestSansToken);
        assertEquals(400, response1.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response1.getBody().get("message"));

        Map<String, String> requestSansPassword = Map.of("token", "abc123");
        ResponseEntity<Map<String, String>> response2 = authController.resetPassword(requestSansPassword);
        assertEquals(400, response2.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response2.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn200_whenResetIsSuccessful() {
        String token = "valid-token";
        String nouveauMotDePasse = "NewPassword123!";
        Map<String, String> request = Map.of(
                "token", token,
                "nouveauMotDePasse", nouveauMotDePasse
        );

        when(tokenService.resetPassword(token, nouveauMotDePasse)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = authController.resetPassword(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mot de passe réinitialisé avec succès.", response.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn400_whenTokenIsInvalid() {
        String token = "invalid-or-expired-token";
        String nouveauMotDePasse = "SomeNewPassword!";
        Map<String, String> request = Map.of(
                "token", token,
                "nouveauMotDePasse", nouveauMotDePasse
        );

        when(tokenService.resetPassword(token, nouveauMotDePasse)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = authController.resetPassword(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Token invalide ou expiré.", response.getBody().get("message"));
    }

    @Test
    void resetPassword_shouldReturn400_whenFieldsAreMissing() {
        Map<String, String> emptyRequest = Map.of();

        ResponseEntity<Map<String, String>> response1 = authController.resetPassword(emptyRequest);
        assertEquals(400, response1.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response1.getBody().get("message"));

        Map<String, String> missingToken = Map.of("token", "", "nouveauMotDePasse", "password123");
        ResponseEntity<Map<String, String>> response2 = authController.resetPassword(missingToken);
        assertEquals(400, response2.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response2.getBody().get("message"));

        Map<String, String> missingPassword = Map.of("token", "validToken", "nouveauMotDePasse", "");
        ResponseEntity<Map<String, String>> response3 = authController.resetPassword(missingPassword);
        assertEquals(400, response3.getStatusCodeValue());
        assertEquals("Token et nouveau mot de passe requis.", response3.getBody().get("message"));
    }

}