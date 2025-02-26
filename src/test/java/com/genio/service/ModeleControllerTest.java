package com.genio.service;

import com.genio.dto.output.ModeleDTO;
import com.genio.exception.business.NoErrorFoundException;
import com.genio.exception.business.NoTemplatesAvailableException;
import com.genio.exception.business.TemplateAlreadyExistsException;
import com.genio.exception.business.TemplateInUseException;
import com.genio.service.impl.ErreurService;
import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ModeleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModeleService modeleService;

    @MockBean
    private ErreurService erreurService;


    @Test
    public void testGetAllTemplatesNoContent() throws Exception {
        Mockito.when(modeleService.getAllTemplates()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/templates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllTemplates() throws Exception {
        Mockito.when(modeleService.getAllTemplates()).thenReturn(
                Arrays.asList(
                        new ModeleDTO(1L, "Convention 2024", "2024", "docx"),
                        new ModeleDTO(2L, "Convention 2023", "Ancien modèle de convention", "docx")
                )
        );

        mockMvc.perform(get("/templates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Convention 2024"))
                .andExpect(jsonPath("$[0].format").value("docx"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nom").value("Convention 2023"))
                .andExpect(jsonPath("$[1].format").value("docx"));
    }

    @Test
    public void testCreateTemplate() throws Exception {
        ModeleDTO modeleDTO = new ModeleDTO(null, "Convention Stage 2024", "2024", "docx");

        Mockito.when(modeleService.createTemplate(Mockito.any(ModeleDTO.class))).thenReturn(
                new ModeleDTO(1L, "Convention Stage 2024", "2024", "docx")
        );

        mockMvc.perform(post("/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Convention Stage 2024\", \"annee\": \"2024\", \"format\": \"docx\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Convention Stage 2024"))
                .andExpect(jsonPath("$.format").value("docx"));
    }

    @Test
    public void testUpdateTemplate() throws Exception {
        ModeleDTO modeleDTO = new ModeleDTO(1L, "Convention Stage 2024", "2024", "docx");

        Mockito.when(modeleService.updateTemplate(Mockito.anyLong(), Mockito.any(ModeleDTO.class))).thenReturn(
                new ModeleDTO(1L, "Convention Stage 2024", "2024", "docx")
        );

        mockMvc.perform(put("/templates/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Convention Stage 2024\", \"annee\": \"2024\", \"format\": \"docx\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Convention Stage 2024"))
                .andExpect(jsonPath("$.format").value("docx"));
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        Mockito.doNothing().when(modeleService).deleteTemplate(Mockito.anyLong());

        mockMvc.perform(delete("/templates/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Modèle supprimé avec succès."));
    }

    @Test
    public void testGetTemplateByIdNotFound() throws Exception {
        Mockito.when(modeleService.getTemplateById(Mockito.anyLong())).thenThrow(NoTemplatesAvailableException.class);

        mockMvc.perform(get("/templates/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTemplateNotFound() throws Exception {
        Mockito.doThrow(NoTemplatesAvailableException.class).when(modeleService).deleteTemplate(Mockito.anyLong());

        mockMvc.perform(delete("/templates/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTemplateInUse() throws Exception {
        Mockito.doThrow(TemplateInUseException.class).when(modeleService).deleteTemplate(Mockito.anyLong());

        mockMvc.perform(delete("/templates/{id}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Modèle en cours d'utilisation"));
    }
    @Test
    public void testCreateTemplateAlreadyExists() throws Exception {
        Mockito.when(modeleService.createTemplate(Mockito.any(ModeleDTO.class))).thenThrow(TemplateAlreadyExistsException.class);

        mockMvc.perform(post("/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Convention Stage 2024\", \"annee\": \"2024\", \"format\": \"docx\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Un modèle avec ce nom existe déjà"));
    }


    @Test
    public void testUpdateTemplateNotFound() throws Exception {
        Mockito.when(modeleService.updateTemplate(Mockito.anyLong(), Mockito.any(ModeleDTO.class)))
                .thenThrow(NoTemplatesAvailableException.class);

        mockMvc.perform(put("/templates/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Updated Model\", \"annee\": \"2025\", \"format\": \"docx\"}"))
                .andExpect(status().isNotFound());
    }




}