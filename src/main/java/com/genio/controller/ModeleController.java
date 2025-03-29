package com.genio.controller;
import com.genio.dto.output.ModeleDTOForList;
import com.genio.exception.business.*;
import com.genio.service.impl.ModeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.genio.exception.business.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;
import com.genio.exception.business.ValidationException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

//dez

@RestController
@RequestMapping("/conventionServices")
public class ModeleController {

    @Autowired
    private ModeleService modeleService;
    private static final Logger logger = LoggerFactory.getLogger(ModeleController.class);

    @GetMapping
    public ResponseEntity<?> getAllModelConvention() {
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            return ResponseEntity.status(204).body(Collections.singletonMap("error", "Aucun modèle de convention disponible"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModelConventionById(@PathVariable Long id) {
        try {
            ModeleDTO modeleDTO = modeleService.getConventionServiceById(id);
            return ResponseEntity.ok(modeleDTO);
        } catch (ConventionServiceNotFoundException e) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Modèle introuvable"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createModelConvention(@RequestParam("nom") String nom,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            ModeleDTO createdModel = modeleService.createModelConvention(nom, file);
            return ResponseEntity.status(201).body(Collections.singletonMap("message", "ModelConvention ajouté avec succès"));
        } catch (ModelConventionAlreadyExistsException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Un modèle avec ce nom existe déjà"));
        } catch (InvalidFormatException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Format non supporté, uniquement .docx accepté"));
        } catch (MissingVariableException e) {
            logger.error("Erreur : {}", e.getMessage());
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        } catch (DatabaseInsertionException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Erreur lors de l'enregistrement du modèle"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Erreur lors de la lecture du fichier DOCX"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateModelConvention(@PathVariable Integer id, @RequestBody ModeleDTO modeleDTO) {
        try {
            modeleService.updateModelConvention(id, modeleDTO);
            return ResponseEntity.ok(Collections.singletonMap("message", "ModelConvention mis à jour avec succès !"));
        } catch (ModelConventionNotFoundException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        } catch (ValidationException | UnauthorizedModificationException | IntegrityCheckFailedException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Erreur interne du serveur"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModelConvention(@PathVariable Long id) {
        try {
            modeleService.deleteModelConvention(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "ModelConvention supprimé avec succès !"));
        } catch (ModelConventionNotFoundException | ModelConventionInUseException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        } catch (DeletionFailedException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Erreur interne du serveur"));
        }
    }

    @GetMapping("/{id}/isUsed")
    public ResponseEntity<?> isModelUsed(@PathVariable Long id) {
        try {
            boolean isUsed = modeleService.isModelInUse(id);
            return ResponseEntity.ok(Collections.singletonMap("isUsed", isUsed));
        } catch (ModelConventionNotFoundException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}