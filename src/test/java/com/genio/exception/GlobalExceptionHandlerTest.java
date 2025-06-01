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

package com.genio.exception;

import com.genio.controller.ExceptionTriggerController;
import com.genio.service.impl.HistorisationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExceptionTriggerController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private HistorisationService historisationService;
    @Test
    @WithMockUser
    void shouldHandleInvalidFileFormatException() throws Exception {
        mockMvc.perform(get("/test/invalid-format"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Format de fichier invalide"));
    }

    @Test
    @WithMockUser
    void shouldHandleEmptyFileException() throws Exception {
        mockMvc.perform(get("/test/empty-file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fichier vide"));
    }

    @Test
    @WithMockUser
    void shouldHandleFileTooLargeException() throws Exception {
        mockMvc.perform(get("/test/file-too-large"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fichier trop volumineux"));
    }

    @Test
    @WithMockUser
    void shouldHandleEmptyDirectoryException() throws Exception {
        mockMvc.perform(get("/test/empty-dir"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Répertoire vide"));
    }

    @Test
    @WithMockUser
    void shouldHandleConventionServiceAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/already-exists"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Un modèle avec ce nom existe déjà"));
    }

    @Test
    @WithMockUser
    void shouldHandleConventionServiceInUseException() throws Exception {
        mockMvc.perform(get("/test/in-use"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Modèle en cours d'utilisation"));
    }

    @Test
    @WithMockUser
    void shouldHandleInvalidFilterException() throws Exception {
        mockMvc.perform(get("/test/invalid-filter"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Filtre invalide"));
    }

    @Test
    @WithMockUser
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Une erreur interne est survenue")));
    }
}