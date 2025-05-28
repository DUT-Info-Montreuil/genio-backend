package com.genio.controller;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.service.impl.GenioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
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

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    void genererConvention_shouldReturn200_whenSuccess() throws Exception {
        ConventionBinaireRes res = new ConventionBinaireRes(true, "mocked-content".getBytes(), null);

        when(genioService.generateConvention(any(), any())).thenReturn(res);

        String jsonInput = """
                {
                   "etudiant": {
                     "nom": "Dupont",
                     "prenom": "Marie",
                     "email": "marie.durand@example.com",
                     "adresse": "123 Rue Principale",
                     "telephone": "01.23.45.67.89",
                     "sexe": "H",
                     "dateNaissance": "1999-05-21",
                     "cpam": "CPAM Paris",
                     "promotion": "BUT2"
                   },
                   "maitreDeStage": {
                     "nom": "Martin",
                     "prenom": "Paul",
                     "email": "paul.martin@example.com",
                     "fonction": "Responsable",
                     "telephone": "09.87.65.43.21"
                   },
                   "tuteur": {
                     "nom": "Durand",
                     "prenom": "Marie",
                     "email": "marie.durand@example.com"
                   },
                   "organisme": {
                     "nom": "Entreprise X",
                     "adresse": "456 Rue des Champs",
                     "nomRepresentant": "Julie Blanc",
                     "qualiteRepresentant": "Directrice",
                     "nomDuService": "Informatique",
                     "telephone": "07.89.45.61.23",
                     "email": "contact@entreprise-x.com",
                     "lieuDuStage": "Paris"
                   },
                   "stage": {
                     "sujetDuStage": "Développement d'une application web",
                     "dateDebutStage": "2025-01-15",
                     "dateFinStage": "2025-07-15",
                     "duree": "6 mois",
                     "joursTot": 180,
                     "heuresTot": 720,
                     "remunerationHoraire": "15€",
                     "saeStageProfessionnel": "Professionnel",
                     "anneeStage": "2025"
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
                     "email": "marie.durand@example.com",
                     "adresse": "123 Rue Principale",
                     "telephone": "01.23.45.67.89",
                     "sexe": "H",
                     "dateNaissance": "1999-05-21",
                     "cpam": "CPAM Paris",
                     "promotion": "BUT2"
                   },
                   "maitreDeStage": {
                     "nom": "Martin",
                     "prenom": "Paul",
                     "email": "paul.martin@example.com",
                     "fonction": "Responsable",
                     "telephone": "09.87.65.43.21"
                   },
                   "tuteur": {
                     "nom": "Durand",
                     "prenom": "Marie",
                     "email": "marie.durand@example.com"
                   },
                   "organisme": {
                     "nom": "Entreprise X",
                     "adresse": "456 Rue des Champs",
                     "nomRepresentant": "Julie Blanc",
                     "qualiteRepresentant": "Directrice",
                     "nomDuService": "Informatique",
                     "telephone": "07.89.45.61.23",
                     "email": "contact@entreprise-x.com",
                     "lieuDuStage": "Paris"
                   },
                   "stage": {
                     "sujetDuStage": "Développement d'une application web",
                     "dateDebutStage": "2025-01-15",
                     "dateFinStage": "2025-07-15",
                     "duree": "6 mois",
                     "joursTot": 180,
                     "heuresTot": 720,
                     "remunerationHoraire": "15€",
                     "saeStageProfessionnel": "Professionnel",
                     "anneeStage": "2025"
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
