package com.genio.controller;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.service.GenioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class GenioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenioService genioService;

    @Test
    void genererConvention_shouldReturn200_whenSuccess() throws Exception {
        ConventionBinaireRes res = new ConventionBinaireRes(true, "mocked-content".getBytes(), null);

        when(genioService.generateConvention(any(), any())).thenReturn(res);

        String jsonInput = """
                {
                  "etudiant": {
                    "nom": "Dupont",
                    "prenom": "Marie",
                    "sexe": "F",
                    "dateNaissance": "2002-05-15",
                    "adresse": "1 rue Paris",
                    "telephone": "0123456789",
                    "email": "test@test.com",
                    "cpam": "CPAM"
                  },
                  "maitreDeStage": {
                    "nom": "Martin",
                    "prenom": "Paul",
                    "fonction": "Responsable",
                    "telephone": "0123456789",
                    "email": "paul@test.com"
                  },
                  "organisme": {
                    "nom": "Entreprise",
                    "adresse": "Adresse",
                    "nomRepresentant": "Mr X",
                    "qualiteRepresentant": "Dir",
                    "nomDuService": "Dev",
                    "telephone": "0123456789",
                    "email": "orga@test.com",
                    "lieuDuStage": "Paris"
                  },
                  "stage": {
                    "anneeStage": "2025",
                    "sujetDuStage": "Sujet",
                    "dateDebutStage": "2025-01-01",
                    "dateFinStage": "2025-06-30",
                    "duree": "6 mois",
                    "joursTot": 100,
                    "heuresTot": 700,
                    "remunerationHoraire": "10€",
                    "saeStageProfessionnel": "Oui"
                  },
                  "tuteur": {
                    "nom": "Durand",
                    "prenom": "Luc",
                    "email": "luc@iut.fr"
                  },
                  "modeleId": 1
                }
                """;

        mockMvc.perform(post("/api/genio/generer")
                        .param("formatFichierOutput", "DOCX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk());
    }

    @Test
    void genererConvention_shouldReturn400_whenConventionGenerationFails() throws Exception {
        ConventionBinaireRes res = new ConventionBinaireRes(false, null, "Erreur de validation");

        when(genioService.generateConvention(any(), any())).thenReturn(res);

        String jsonInput = """
                {
                  "etudiant": {
                    "nom": "Dupont"
                  },
                  "modeleId": 1
                }
                """;

        mockMvc.perform(post("/api/genio/generer")
                        .param("formatFichierOutput", "DOCX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genererConvention_shouldReturn500_whenExceptionOccurs() throws Exception {
        when(genioService.generateConvention(any(), any()))
                .thenThrow(new RuntimeException("Erreur serveur"));

        String jsonInput = """
            {
              "etudiant": {
                "nom": "Dupont",
                "prenom": "Marie",
                "sexe": "F",
                "dateNaissance": "2002-05-15",
                "adresse": "1 rue Paris",
                "telephone": "0123456789",
                "email": "test@test.com",
                "cpam": "CPAM"
              },
              "maitreDeStage": {
                "nom": "Martin",
                "prenom": "Paul",
                "fonction": "Responsable",
                "telephone": "0123456789",
                "email": "paul@test.com"
              },
              "organisme": {
                "nom": "Entreprise",
                "adresse": "Adresse",
                "nomRepresentant": "Mr X",
                "qualiteRepresentant": "Dir",
                "nomDuService": "Dev",
                "telephone": "0123456789",
                "email": "orga@test.com",
                "lieuDuStage": "Paris"
              },
              "stage": {
                "anneeStage": "2025",
                "sujetDuStage": "Sujet",
                "dateDebutStage": "2025-01-01",
                "dateFinStage": "2025-06-30",
                "duree": "6 mois",
                "joursTot": 100,
                "heuresTot": 700,
                "remunerationHoraire": "10€",
                "saeStageProfessionnel": "Oui"
              },
              "tuteur": {
                "nom": "Durand",
                "prenom": "Luc",
                "email": "luc@iut.fr"
              },
              "modeleId": 1
            }
            """;

        mockMvc.perform(post("/api/genio/generer")
                        .param("formatFichierOutput", "DOCX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isInternalServerError());
    }



}
