package com.genio.controller;
import com.genio.exception.business.*;
import com.genio.service.impl.ModeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.genio.exception.business.InvalidFormatException;
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
    public ResponseEntity<?> getAllModelConvention() {
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            return ResponseEntity.status(204).body(Collections.singletonMap(KEY_ERROR, "Aucun modèle de convention disponible"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModelConventionById(@PathVariable Long id) {
        try {
            ModeleDTO modeleDTO = modeleService.getConventionServiceById(id);
            return ResponseEntity.ok(modeleDTO);
        } catch (ConventionServiceNotFoundException e) {
            return ResponseEntity.status(404).body(Collections.singletonMap(KEY_ERROR, "Modèle introuvable"));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createModelConvention(@RequestParam("nom") String nom,
                                                                     @RequestParam("file") MultipartFile file) {
        try {
            modeleService.createModelConvention(nom, file);
            return ResponseEntity.status(201).body(Collections.singletonMap(KEY_MESSAGE, "ModelConvention ajouté avec succès"));
        } catch (ModelConventionAlreadyExistsException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, "Un modèle avec ce nom existe déjà"));
        } catch (InvalidFileFormatException | InvalidFormatException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, "Format non supporté, uniquement .docx accepté"));
        } catch (MissingVariableException e) {
            logger.error("Erreur : {}", e.getMessage());
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        } catch (DatabaseInsertionException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur lors de l'enregistrement du modèle"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur lors de la lecture du fichier DOCX"));
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
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap(KEY_ERROR, "Erreur interne du serveur"));
        }
    }

    @GetMapping("/{id}/isUsed")
    public ResponseEntity<?> isModelUsed(@PathVariable Long id) {
        try {
            boolean isUsed = modeleService.isModelInUse(id);
            return ResponseEntity.ok(Collections.singletonMap("isUsed", isUsed));
        } catch (ModelConventionNotFoundException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap(KEY_ERROR, e.getMessage()));
        }
    }

    public ModeleController(ModeleService modeleService) {
        this.modeleService = modeleService;
    }
}