package com.genio.service;

import com.genio.service.impl.ModeleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.Mockito.when;
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
    public void testGetAllConventionServicesNoContent() throws Exception {
        when(modeleService.getAllConventionServices()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/conventionServices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

  /* @Test
    public void testGetConventionServiceById() throws Exception {
        // Arrange
        ModeleDTO modeleDTO = new ModeleDTO(
                1L,  // id
                "Convention 2024",  // nom
                "2024",  // année
                "docx",  // format
                "Modèle standard de convention 2024",  // description
                "2024-01-10"  // date de création
        );
        when(modeleService.getConventionServiceById(1L)).thenReturn(modeleDTO);

        // Act & Assert
        mockMvc.perform(get("/conventionServices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Convention 2024"))
                .andExpect(jsonPath("$.dateCreation").value("2024-01-10"));
    }*/
}
