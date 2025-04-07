package com.genio.controller;
import com.genio.exception.business.*;
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
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";


    private static final Logger logger = LoggerFactory.getLogger(ModeleController.class);

    @GetMapping
    public ResponseEntity<Object> getAllModelConvention() {
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            Map<String, String> error = Map.of(KEY_ERROR, "Aucun modèle de convention disponible");
            return ResponseEntity.status(204).body(error);
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
    public ResponseEntity<Map<String, String>> createModelConvention(@RequestParam("file") MultipartFile file) {
        try {
            modeleService.createModelConvention(file);
            return ResponseEntity.status(201).body(Collections.singletonMap(KEY_MESSAGE, "ModelConvention ajouté avec succès"));
        } catch (ModelConventionAlreadyExistsException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, "Un modèle avec ce nom existe déjà"));
        } catch (InvalidFileFormatException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, "Format non supporté, uniquement .docx accepté"));
        } catch (MissingVariableException e) {
            logger.error("Erreur : {}", e.getMessage());
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
        } catch (ModelConventionNotFoundException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (ValidationException | UnauthorizedModificationException | IntegrityCheckFailedException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur interne du serveur"));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteModelConvention(@PathVariable Long id) {
        try {
            modeleService.deleteModelConvention(id);
            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "ModelConvention supprimé avec succès !"));
        } catch (ModelConventionNotFoundException | ModelConventionInUseException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (DeletionFailedException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
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

    public ModeleController(ModeleService modeleService) {
        this.modeleService = modeleService;
    }
}