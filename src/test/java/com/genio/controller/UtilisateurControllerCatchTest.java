package com.genio.controller;

import com.genio.exception.business.EmailDejaUtiliseException;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;
import com.genio.service.impl.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UtilisateurControllerCatchTest {

    @MockBean
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurController utilisateurController;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testCreer_CatchEmailDejaUtiliseException() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setEmail("test@example.com");

        doThrow(new EmailDejaUtiliseException("Email déjà utilisé"))
                .when(utilisateurService).creerUtilisateur(dto);

        ResponseEntity<Map<String, String>> response = utilisateurController.creer(dto);

        assertEquals(409, response.getStatusCode().value());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Email déjà utilisé", response.getBody().get("error"));
    }

    @Test
    void testCreer_CatchGenericException() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setEmail("test@example.com");

        doThrow(new RuntimeException("Erreur inattendue"))
                .when(utilisateurService).creerUtilisateur(dto);

        ResponseEntity<Map<String, String>> response = utilisateurController.creer(dto);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Une erreur est survenue : Erreur inattendue", response.getBody().get("error"));
    }

    @Test
    void testGetUtilisateurConnecte_Success() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        Utilisateur utilisateurMock = new Utilisateur();
        utilisateurMock.setEmail("user@example.com");

        when(utilisateurService.getByEmail("user@example.com"))
                .thenReturn(Optional.of(utilisateurMock));

        ResponseEntity<Utilisateur> response = utilisateurController.getUtilisateurConnecte(userDetails);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());
    }

    @Test
    void testGetUtilisateurConnecte_NotFound() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user_inexistant@example.com");

        when(utilisateurService.getByEmail("user_inexistant@example.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<Utilisateur> response = utilisateurController.getUtilisateurConnecte(userDetails);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetUtilisateursNonActifs() {
        List<Utilisateur> utilisateursMock = List.of(
                new Utilisateur(/* initialise comme tu veux */),
                new Utilisateur(/* ... */)
        );

        when(utilisateurService.getUtilisateursNonActifs()).thenReturn(utilisateursMock);

        ResponseEntity<List<Utilisateur>> response = utilisateurController.getUtilisateursNonActifs();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(utilisateursMock.size(), response.getBody().size());
    }
}