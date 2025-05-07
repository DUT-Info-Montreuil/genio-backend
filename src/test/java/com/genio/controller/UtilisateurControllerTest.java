package com.genio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genio.dto.UtilisateurDTO;
import com.genio.dto.UtilisateurUpdateDTO;
import com.genio.model.Utilisateur;
import com.genio.service.impl.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(controllers = UtilisateurController.class)
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @Autowired
    private ObjectMapper objectMapper;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = Utilisateur.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jdupont@gmail.com")
                .motDePasse("password")
                .role("CONSULTATION")
                .actif(true)
                .build();
    }

    @Test
    void testCreerUtilisateur() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setEmail("jdupont@gmail.com");
        dto.setMotDePasse("password");

        Mockito.when(utilisateurService.creerUtilisateur(any(UtilisateurDTO.class))).thenReturn(utilisateur);

        mockMvc.perform(post("/api/utilisateurs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())  // <-- ici le bon code HTTP attendu : 201
                .andExpect(jsonPath("$.message").value("Utilisateur créé avec succès."));
    }

    @Test
    void testGetAllUtilisateurs() throws Exception {
        Mockito.when(utilisateurService.getAllUtilisateurs()).thenReturn(List.of(utilisateur));

        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jdupont@gmail.com"))
                .andExpect(jsonPath("$[0].nom").value("Dupont"))
                .andExpect(jsonPath("$[0].prenom").value("Jean"));
    }

    @Test
    void testSupprimerUtilisateur() throws Exception {
        mockMvc.perform(delete("/api/utilisateurs/1").with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(utilisateurService).supprimerUtilisateur(1L);
    }

    @Test
    void testModifierUtilisateur_Success() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("NewNom");
        dto.setPrenom("NewPrenom");
        dto.setEmail("newusername@gmail.com");
        dto.setMotDePasse("newpassword");

        Mockito.when(utilisateurService.modifierUtilisateur(eq(1L), any(UtilisateurDTO.class)))
                .thenReturn(Optional.of(utilisateur));

        mockMvc.perform(put("/api/utilisateurs/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Dupont"));
    }

    @Test
    void testModifierUtilisateur_NotFound() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();

        Mockito.when(utilisateurService.modifierUtilisateur(eq(1L), any(UtilisateurDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/utilisateurs/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testModifierRoleEtActivation() throws Exception {
        Mockito.when(utilisateurService.modifierRoleEtActivation(1L, "ADMIN", true))
                .thenReturn(utilisateur);

        mockMvc.perform(put("/api/utilisateurs/1/role-activation")
                        .with(csrf())
                        .param("role", "ADMIN")
                        .param("actif", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("CONSULTATION"));
    }

    @Test
    void testAdminUpdate_Success() throws Exception {
        UtilisateurUpdateDTO updateDTO = new UtilisateurUpdateDTO();
        updateDTO.setRole("GESTIONNAIRE");
        updateDTO.setActif(true);

        Mockito.when(utilisateurService.modifierRoleEtStatut(1L, "GESTIONNAIRE", true))
                .thenReturn(Optional.of(utilisateur));

        mockMvc.perform(put("/api/utilisateurs/1/admin-update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jdupont@gmail.com"))
                .andExpect(jsonPath("$.role").value("CONSULTATION"))
                .andExpect(jsonPath("$.nom").value("Dupont"));
    }

    @Test
    void testAdminUpdate_NotFound() throws Exception {
        UtilisateurUpdateDTO updateDTO = new UtilisateurUpdateDTO();
        updateDTO.setRole("GESTIONNAIRE");
        updateDTO.setActif(true);

        Mockito.when(utilisateurService.modifierRoleEtStatut(1L, "GESTIONNAIRE", true))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/utilisateurs/1/admin-update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }


}