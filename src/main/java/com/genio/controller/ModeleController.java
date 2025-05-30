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
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

package com.genio.controller;
import com.genio.exception.business.*;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.ModeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.genio.exception.business.ValidationException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.genio.dto.outputmodeles.ModeleDTOForList;
import com.genio.dto.outputmodeles.ModeleDTO;


@RestController
@RequestMapping("/conventionServices")
public class ModeleController {

    private final ModeleService modeleService;
    private final ModeleRepository modeleRepository;
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";
    private static final Logger logger = LoggerFactory.getLogger(ModeleController.class);

    @GetMapping
    public ResponseEntity<Object> getAllModelConvention() {
        logger.info("Requête reçue : récupération de tous les modèles non archivés.");
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            logger.info("Nombre de modèles trouvés : {}", conventionServices.size());
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            logger.warn("Aucun modèle de convention disponible.");
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getModelConventionById(@PathVariable Long id) {
        logger.info("Requête reçue : récupération du modèle avec ID {}", id);
        try {
            ModeleDTO modeleDTO = modeleService.getConventionServiceById(id);
            logger.info("Modèle trouvé : {}", modeleDTO.getNom());
            return ResponseEntity.ok().body(modeleDTO);
        } catch (ConventionServiceNotFoundException e) {
            logger.warn("Modèle non trouvé avec ID {}", id);
            return ResponseEntity.status(404).body(Map.of(KEY_ERROR, "Modèle introuvable"));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createModelConvention(
            @RequestParam("file") MultipartFile file,
            @RequestParam("annee") String annee,
            @RequestParam("titre") String titre
    ) {
        logger.info("Requête reçue : ajout d'un modèle pour l'année {} avec le titre '{}'", annee, titre);
        try {
            ModeleDTO result = modeleService.createModelConvention(file, annee, titre);
            logger.info("Modèle ajouté avec succès : {}", result.getNom());
            return ResponseEntity.status(201).body(Collections.singletonMap(KEY_MESSAGE,
                    "Le modèle " + result.getTitre() + " a été ajouté avec succès !"));
        } catch (ModelConventionAlreadyExistsException | InvalidFileFormatException | MissingVariableException e) {
            logger.warn("Erreur côté client lors de l'ajout du modèle : {}", e.getMessage());
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (DatabaseInsertionException | IOException e) {
            logger.error("Erreur serveur lors de l'enregistrement du modèle", e);
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur lors de l'enregistrement du modèle"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateModelConvention(@PathVariable Integer id, @RequestBody ModeleDTO modeleDTO) {
        logger.info("Requête reçue : mise à jour du modèle ID {}", id);
        try {
            modeleService.updateModelConvention(id, modeleDTO);
            logger.info("Mise à jour réussie du modèle ID {}", id);
            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "ModelConvention mis à jour avec succès !"));
        } catch (ModelConventionNotFoundException | ValidationException | IntegrityCheckFailedException e) {
            logger.warn("Erreur de validation ou intégrité lors de la mise à jour : {}", e.getMessage());
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (Exception e) {
            logger.error("Erreur interne du serveur lors de la mise à jour", e);
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur interne du serveur"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> archiveModelConvention(@PathVariable Long id) {
        logger.info("Requête reçue : archivage du modèle ID {}", id);
        try {
            modeleService.archiveModelConvention(id);
            logger.info("Modèle archivé avec succès : ID {}", id);
            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "Modèle archivé avec succès !"));
        } catch (ModelConventionNotFoundException | ModelConventionInUseException e) {
            logger.warn("Échec de l'archivage du modèle ID {} : {}", id, e.getMessage());
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/{id}/isUsed")
    public ResponseEntity<Object> isModelUsed(@PathVariable Long id) {
        logger.info("Vérification d'utilisation du modèle ID {}", id);
        try {
            boolean isUsed = modeleService.isModelInUse(id);
            logger.info("Modèle ID {} est-il utilisé ? {}", id, isUsed);
            return ResponseEntity.ok(Map.of("isUsed", isUsed));
        } catch (ModelConventionNotFoundException e) {
            logger.warn("Modèle non trouvé lors de la vérification d'utilisation : ID {}", id);
            return ResponseEntity.status(400).body(Map.of(KEY_ERROR, e.getMessage()));
        }
    }

    public ModeleController(ModeleService modeleService, ModeleRepository modeleRepository) {
        this.modeleService = modeleService;
        this.modeleRepository = modeleRepository;
    }


    @PostMapping("/test-generation")
    public ResponseEntity<String> testDocxVars(@RequestParam("file") MultipartFile file) {
        logger.info("Test de génération : extraction des variables du modèle envoyé.");
        try {
            List<String> variables = modeleService.extractRawVariables(file);

            if (variables == null || variables.isEmpty()) {
                logger.warn("Aucune variable trouvée dans le fichier.");
                return ResponseEntity.status(400).body("Aucun contenu exploitable détecté.");
            }

            if (logger.isInfoEnabled()) {
                 logger.info("Variables détectées : {}", String.join(", ", variables));
            }
            return ResponseEntity.ok("Variables détectées : " + String.join(", ", variables));
        } catch (Exception e) {
            logger.error("Erreur lors de l'extraction des variables du modèle", e);
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/check-nom-exists")
    public ResponseEntity<Map<String, Boolean>> checkModelNameExists(@RequestParam("annee") String annee) {
        String nom = "modeleConvention_" + annee + ".docx";
        boolean exists = modeleRepository.findFirstByNomAndArchivedFalse(nom).isPresent();
        logger.info("Vérification de l'existence du nom de modèle {} : {}", nom, exists);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PutMapping("/{id}/file")
    public ResponseEntity<Map<String, String>> updateModelFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        logger.info("Remplacement du fichier du modèle ID {}", id);
        try {
            modeleService.replaceModelFile(id, file);
            logger.info("Fichier remplacé avec succès pour le modèle ID {}", id);
            return ResponseEntity.ok(Map.of(KEY_MESSAGE, "Fichier remplacé avec succès"));
        } catch (ModelConventionAlreadyExistsException e) {
            logger.warn("Fichier déjà existant : {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of(KEY_ERROR, "Ce fichier est déjà utilisé dans un autre modèle actif."));
        } catch (ModelConventionNotFoundException e) {
            logger.warn("Modèle introuvable : {}", e.getMessage());
            return ResponseEntity.status(404).body(Map.of(KEY_ERROR, "Modèle introuvable."));
        } catch (IOException e) {
            logger.error("Erreur IO lors du remplacement du fichier", e);
            return ResponseEntity.status(500).body(Map.of(KEY_ERROR, "Erreur lors de la lecture du fichier."));
        } catch (Exception e) {
            logger.error("Erreur inattendue", e);
            return ResponseEntity.status(500).body(Map.of(KEY_ERROR, "Erreur serveur inattendue."));
        }
    }

    @GetMapping("/archives")
    public ResponseEntity<List<ModeleDTOForList>> getArchivedModels() {
        logger.info("Récupération de la liste des modèles archivés.");
        List<Modele> archives = modeleRepository.findAll()
                .stream()
                .filter(Modele::isArchived)
                .toList();

        if (logger.isInfoEnabled()) {
            logger.info("{} modèle(s) archivé(s) récupéré(s).", archives.size());
        }

        List<ModeleDTOForList> result = archives.stream()
                .map(m -> new ModeleDTOForList(
                        m.getId(),
                        m.getNom(),
                        "Archivé depuis le " + m.getArchivedAt(),
                        "docx",
                        m.getTitre()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ModeleDTOForList>> searchModeles(
            @RequestParam(required = false) String annee,
            @RequestParam(required = false) String titre
    ) {
        logger.info("Recherche de modèles : année = {}, titre = {}", annee, titre);

        List<Modele> found = modeleRepository.findAll().stream()
                .filter(m -> !m.isArchived())
                .filter(m -> annee == null || m.getAnnee().equals(annee))
                .filter(m -> titre == null || m.getTitre().toLowerCase().contains(titre.toLowerCase()))
                .toList();

        if (logger.isInfoEnabled()) {
            logger.info("Nombre de modèles trouvés correspondant aux critères : {}", found.size());
        }

        List<ModeleDTOForList> result = found.stream()
                .map(m -> new ModeleDTOForList(
                        m.getId(),
                        m.getNom(),
                        "Année : " + m.getAnnee(),
                        "docx",
                        m.getTitre()
                )).toList();

        return ResponseEntity.ok(result);
    }
}