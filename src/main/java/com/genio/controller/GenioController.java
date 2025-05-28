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

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;
import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.mapper.ConventionMapper;
import com.genio.model.Historisation;
import com.genio.model.Modele;
import com.genio.repository.HistorisationRepository;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.GenioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/genio")
@Tag(name = "Convention Management", description = "Manage convention generation and operations")
@Validated
public class GenioController {

    private static final Logger logger = LoggerFactory.getLogger(GenioController.class);
    private final GenioService genioService;
    private final HistorisationRepository historisationRepository;

    public GenioController(GenioService genioService, HistorisationRepository historisationRepository, ModeleRepository modeleRepository) {
        this.genioService = genioService;
        this.historisationRepository = historisationRepository;
        this.modeleRepository = modeleRepository;
    }

    @PostMapping("/generer")
    @Operation(summary = "Generate a convention", description = "This endpoint generates a convention based on the input data.")
    public ResponseEntity<ConventionBinaireRes> genererConvention(
            @Parameter(description = "The convention data input", required = true) @Valid @RequestBody ConventionWsDTO input,
            @Parameter(description = "The file format for the generated convention") @RequestParam String formatFichierOutput) {

        logger.info("Requête reçue pour générer une convention avec format : {}", formatFichierOutput);

        try {
            ConventionServiceDTO serviceInput = ConventionMapper.toServiceDTO(input);
            ConventionBinaireRes result = genioService.generateConvention(serviceInput, formatFichierOutput);

            if (result.isSuccess()) {
                logger.info("Convention générée avec succès.");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Erreur lors de la génération de la convention : {}", result.getMessageErreur());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            logger.error("Erreur inattendue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ConventionBinaireRes(false, null, "Erreur inattendue : " + e.getMessage()));
        }
    }

    @GetMapping("/modele/annee/{annee}")
    public ResponseEntity<List<Modele>> getModelesParAnnee(@PathVariable String annee) {
        try {
            List<Modele> modeles = genioService.getModelesByAnnee(annee);
            return ResponseEntity.ok(modeles);
        } catch (ModelNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/historique")
    public ResponseEntity<List<Historisation>> getHistorique() {
        List<Historisation> historique = historisationRepository.findAll();
        return ResponseEntity.ok(historique);
    }

    private final ModeleRepository modeleRepository;

    @GetMapping("/modele/{id}/download")
    public ResponseEntity<byte[]> downloadModele(@PathVariable Long id) {
        Optional<Modele> opt = modeleRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Modele modele = opt.get();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + modele.getNom())
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .body(modele.getFichierBinaire());
    }


}
