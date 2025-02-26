package com.genio.controller;

import com.genio.dto.output.ErreurDTO;
import com.genio.dto.output.ModeleDTO;
import com.genio.exception.business.NoErrorFoundException;
import com.genio.exception.business.NoTemplatesAvailableException;
import com.genio.exception.business.TemplateNotFoundException;
import com.genio.service.impl.ModeleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
public class ModeleController {

    @Autowired
    private ModeleService modeleService;

    @GetMapping
    public ResponseEntity<?> getAllTemplates() {
        try {
            List<ModeleDTO> templates = modeleService.getAllTemplates();
            return ResponseEntity.ok(templates);
        } catch (NoTemplatesAvailableException e) {
            return ResponseEntity.status(204).body(new ErreurDTO("Aucun modèle de convention disponible"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateById(@PathVariable Long id) {
        try {
            ModeleDTO modeleDTO = modeleService.getTemplateById(id);
            return ResponseEntity.ok(modeleDTO);
        } catch (TemplateNotFoundException e) {
            ErreurDTO erreurDTO = new ErreurDTO("Modèle introuvable");
            return ResponseEntity.status(404).body(erreurDTO);
        }
    }

    @PostMapping
    public ResponseEntity<ModeleDTO> createTemplate(@RequestBody ModeleDTO modeleDTO) {
        ModeleDTO createdTemplate = modeleService.createTemplate(modeleDTO);
        return ResponseEntity.status(201).body(createdTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeleDTO> updateTemplate(@PathVariable Long id, @RequestBody ModeleDTO modeleDTO) {
        try {
            ModeleDTO updatedTemplate = modeleService.updateTemplate(id, modeleDTO);
            return ResponseEntity.ok(updatedTemplate);
        } catch (NoTemplatesAvailableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTemplate(@PathVariable Long id) {
        try {
            modeleService.deleteTemplate(id);
            return ResponseEntity.ok("Modèle supprimé avec succès.");
        } catch (NoTemplatesAvailableException e) {
            return ResponseEntity.notFound().build();
        }
    }

}