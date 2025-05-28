package com.genio.controller;

import com.genio.repository.ModeleRepository;
import com.genio.service.impl.HistorisationService;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ModeleController.class)
class ModeleControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HistorisationService historisationService;

    @MockBean
    private ModeleService modeleService;

    @MockBean
    private ModeleRepository modeleRepository;


    @Test
    void testDocxVars_returnsVariables_whenFileHasContent() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "content".getBytes());

        List<String> mockVars = List.of("NOM_ETUDIANT", "PRENOM_ETUDIANT");
        when(modeleService.extractRawVariables(any())).thenReturn(mockVars);

        mockMvc.perform(multipart("/conventionServices/test-generation")
                        .file(file)
                        .with(csrf())
                        .with(user("test").roles("ADMIN"))) // ← Simule un utilisateur authentifié avec un rôle
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Variables détectées")))
                .andExpect(content().string(containsString("NOM_ETUDIANT")))
                .andExpect(content().string(containsString("PRENOM_ETUDIANT")));
    }
}