package com.genio.exception;


import com.genio.controller.ExceptionTriggerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExceptionTriggerController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleInvalidFileFormatException() throws Exception {
        mockMvc.perform(get("/test/invalid-format"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Format de fichier invalide"));
    }

    @Test
    void shouldHandleEmptyFileException() throws Exception {
        mockMvc.perform(get("/test/empty-file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fichier vide"));
    }

    @Test
    void shouldHandleFileTooLargeException() throws Exception {
        mockMvc.perform(get("/test/file-too-large"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fichier trop volumineux"));
    }

    @Test
    void shouldHandleEmptyDirectoryException() throws Exception {
        mockMvc.perform(get("/test/empty-dir"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Répertoire vide"));
    }

    @Test
    void shouldHandleConventionServiceAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/already-exists"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Un modèle avec ce nom existe déjà"));
    }

    @Test
    void shouldHandleConventionServiceInUseException() throws Exception {
        mockMvc.perform(get("/test/in-use"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Modèle en cours d'utilisation"));
    }

    @Test
    void shouldHandleInvalidFilterException() throws Exception {
        mockMvc.perform(get("/test/invalid-filter"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Filtre invalide"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Une erreur interne est survenue")));
    }
}