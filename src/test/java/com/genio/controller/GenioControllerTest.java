package com.genio.controller;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.model.Historisation;
import com.genio.model.Modele;
import com.genio.repository.HistorisationRepository;
import com.genio.repository.ModeleRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


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

    @MockBean
    private HistorisationRepository historisationRepository;

    @MockBean
    private ModeleRepository modeleRepository;


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

    @Test
    void getModelesParAnnee_shouldReturnList_whenFound() throws Exception {
        when(genioService.getModelesByAnnee("2025")).thenReturn(List.of(new Modele()));

        mockMvc.perform(get("/api/genio/modele/annee/2025"))
                .andExpect(status().isOk());
    }

    @Test
    void getModelesParAnnee_shouldReturn404_whenNotFound() throws Exception {
        when(genioService.getModelesByAnnee("2030")).thenThrow(new ModelNotFoundException("Pas trouvé"));

        mockMvc.perform(get("/api/genio/modele/annee/2030"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHistorique_shouldReturnHistoriqueList() throws Exception {
        when(historisationRepository.findAll()).thenReturn(List.of(new Historisation()));

        mockMvc.perform(get("/api/genio/historique"))
                .andExpect(status().isOk());
    }

    @Test
    void downloadModele_shouldReturn200_whenFound() throws Exception {
        Modele modele = new Modele();
        modele.setNom("test.docx");
        modele.setFichierBinaire("dummy".getBytes());

        when(modeleRepository.findById(1L)).thenReturn(Optional.of(modele));

        mockMvc.perform(get("/api/genio/modele/1/download"))
                .andExpect(status().isOk());
    }

    @Test
    void downloadModele_shouldReturn404_whenNotFound() throws Exception {
        when(modeleRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/genio/modele/999/download"))
                .andExpect(status().isNotFound());
    }

    @Test
    void genererConvention_shouldLogWarningAndReturn400_whenResultIsNotSuccess() throws Exception {
        ConventionBinaireRes res = new ConventionBinaireRes(false, null, "Erreur de validation");
        when(genioService.generateConvention(any(), any())).thenReturn(res);

        String jsonInput = """
            {
              "etudiant": {
                "nom": "Durand",
                "prenom": "Lucie",
                "email": "lucie.durand@example.com",
                "adresse": "12 Rue Exemple",
                "telephone": "06.00.00.00.00",
                "sexe": "F",
                "dateNaissance": "2000-01-01",
                "cpam": "CPAM Marseille",
                "promotion": "BUT3"
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
                "nom": "Entreprise Y",
                "adresse": "789 Rue Projet",
                "nomRepresentant": "Jean Dupont",
                "qualiteRepresentant": "PDG",
                "nomDuService": "Tech",
                "telephone": "07.11.22.33.44",
                "email": "tech@entreprise-y.com",
                "lieuDuStage": "Lyon"
              },
              "stage": {
                "sujetDuStage": "Refonte d’un site e-commerce",
                "dateDebutStage": "2025-02-01",
                "dateFinStage": "2025-08-01",
                "duree": "6 mois",
                "joursTot": 180,
                "heuresTot": 700,
                "remunerationHoraire": "12€",
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
                .andExpect(status().isBadRequest());
    }



}
