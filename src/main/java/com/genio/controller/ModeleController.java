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

    @GetMapping
    public ResponseEntity<Object> getAllModelConvention() {
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getModelConventionById(@PathVariable Long id) {
        try {
            ModeleDTO modeleDTO = modeleService.getConventionServiceById(id);
            return ResponseEntity.ok().body(modeleDTO);
        } catch (ConventionServiceNotFoundException e) {
            Map<String, String> error = Map.of(KEY_ERROR, "Modèle introuvable");
            return ResponseEntity.status(404).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createModelConvention(
            @RequestParam("file") MultipartFile file,
            @RequestParam("annee") String annee,
            @RequestParam("titre") String titre
    ) {
        try {
            ModeleDTO result = modeleService.createModelConvention(file, annee, titre);
            return ResponseEntity.status(201).body(Collections.singletonMap(KEY_MESSAGE,
                    "Le modèle " + result.getTitre() + " a été ajouté avec succès !"));
        } catch (ModelConventionAlreadyExistsException | InvalidFileFormatException | MissingVariableException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (DatabaseInsertionException | IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur lors de l'enregistrement du modèle"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateModelConvention(@PathVariable Integer id, @RequestBody ModeleDTO modeleDTO) {
        try {
            modeleService.updateModelConvention(id, modeleDTO);
            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "ModelConvention mis à jour avec succès !"));
        } catch (ModelConventionNotFoundException | ValidationException | IntegrityCheckFailedException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur interne du serveur"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> archiveModelConvention(@PathVariable Long id) {
        try {
            modeleService.archiveModelConvention(id);
            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "Modèle archivé avec succès !"));
        } catch (ModelConventionNotFoundException | ModelConventionInUseException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/{id}/isUsed")
    public ResponseEntity<Object> isModelUsed(@PathVariable Long id) {
        try {
            boolean isUsed = modeleService.isModelInUse(id);
            return ResponseEntity.ok(Map.of("isUsed", isUsed));
        } catch (ModelConventionNotFoundException e) {
            return ResponseEntity.status(400).body(Map.of(KEY_ERROR, e.getMessage()));
        }
    }

    public ModeleController(ModeleService modeleService, ModeleRepository modeleRepository) {
        this.modeleService = modeleService;
        this.modeleRepository = modeleRepository;
    }


    @PostMapping("/test-generation")
    public ResponseEntity<String> testDocxVars(@RequestParam("file") MultipartFile file) {
        try {
            List<String> variables = modeleService.extractRawVariables(file);

            if (variables == null || variables.isEmpty()) {
                return ResponseEntity.status(400).body("Aucun contenu exploitable détecté.");
            }

            return ResponseEntity.ok("Variables détectées : " + String.join(", ", variables));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/check-nom-exists")
    public ResponseEntity<Map<String, Boolean>> checkModelNameExists(@RequestParam("annee") String annee) {
        String nom = "modeleConvention_" + annee + ".docx";
        boolean exists = modeleRepository.findFirstByNom(nom).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PutMapping("/{id}/file")
    public ResponseEntity<Map<String, String>> updateModelFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            modeleService.replaceModelFile(id, file);
            return ResponseEntity.ok(Map.of(KEY_MESSAGE, "Fichier remplacé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(KEY_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/archives")
    public ResponseEntity<List<ModeleDTOForList>> getArchivedModels() {
        List<Modele> archives = modeleRepository.findAll()
                .stream()
                .filter(Modele::isArchived)
                .toList();

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
        List<Modele> found = modeleRepository.findAll().stream()
                .filter(m -> !m.isArchived())
                .filter(m -> annee == null || m.getAnnee().equals(annee))
                .filter(m -> titre == null || m.getTitre().toLowerCase().contains(titre.toLowerCase()))
                .toList();

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