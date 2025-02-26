package com.genio.controller;
import com.genio.dto.output.ErreurDTO;
import com.genio.dto.output.ModeleDTO;
import com.genio.dto.output.ModeleDTOForList;
import com.genio.dto.output.ModelConventionResponseDTO;
import com.genio.dto.output.ModelConventionErrorResponseDTO;
import com.genio.exception.business.*;
import com.genio.service.impl.ModeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.genio.exception.business.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/conventionServices")
public class ModeleController {

    @Autowired
    private ModeleService modeleService;
    private static final Logger logger = LoggerFactory.getLogger(ModeleController.class);

    @GetMapping
    public ResponseEntity<?> getAllConventionServices() {
        try {
            List<ModeleDTOForList> conventionServices = modeleService.getAllConventionServices();
            return ResponseEntity.ok(conventionServices);
        } catch (NoConventionServicesAvailableException e) {
            return ResponseEntity.status(204).body(new ErreurDTO("Aucun modèle de convention disponible"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConventionServiceById(@PathVariable Long id) {
        try {
            ModeleDTO modeleDTO = modeleService.getConventionServiceById(id);
            return ResponseEntity.ok(modeleDTO);
        } catch (ConventionServiceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErreurDTO("Modèle introuvable"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createModelConvention(@RequestParam("nom") String nom,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            ModeleDTO createdModel = modeleService.createModelConvention(nom, file);
            return ResponseEntity.status(201).body(new ModelConventionResponseDTO("ModelConvention ajouté avec succès", createdModel.getId()));
        } catch (ModelConventionAlreadyExistsException e) {
            return ResponseEntity.status(400).body(new ModelConventionErrorResponseDTO("Un modèle avec ce nom existe déjà"));
        } catch (InvalidFormatException e) {
            return ResponseEntity.status(400).body(new ModelConventionErrorResponseDTO("Format non supporté, uniquement .docx accepté"));
        } catch (MissingVariableException e) {
            logger.error("Erreur : {}", e.getMessage());
            return ResponseEntity.status(400).body(new ModelConventionErrorResponseDTO(e.getMessage()));
        } catch (DatabaseInsertionException e) {
            return ResponseEntity.status(500).body(new ModelConventionErrorResponseDTO("Erreur lors de l'enregistrement du modèle"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ModelConventionErrorResponseDTO("Erreur lors de la lecture du fichier DOCX"));
        }
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        logger.error("Une erreur interne est survenue : {}", ex.getMessage());

        String message;

        if (ex.getMessage().contains("Query did not return a unique result")) {
            message = "Plusieurs modèles ont été trouvés avec ce nom. Veuillez vérifier la base de données.";
        } else {
            message = "Une erreur inattendue est survenue. Veuillez réessayer plus tard.";
        }

        return ResponseEntity.status(500).body(new ModelConventionErrorResponseDTO(message));
    }



    /**
     @PutMapping("/{id}")
      public ResponseEntity<ModeleDTO> updateConventionService(@PathVariable Long id, @RequestBody ModeleDTO modeleDTO) {
          try {
              ModeleDTO updatedConventionService = modeleService.updateConventionService(id, modeleDTO);
              return ResponseEntity.ok(updatedConventionService);
          } catch (NoConventionServicesAvailableException e) {
              return ResponseEntity.notFound().build();
          }
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<String> deleteConventionService(@PathVariable Long id) {
          try {
              modeleService.deleteConventionService(id);
              return ResponseEntity.ok("Modèle supprimé avec succès.");
          } catch (NoConventionServicesAvailableException e) {
              return ResponseEntity.notFound().build();
          }
      }
  **/
}