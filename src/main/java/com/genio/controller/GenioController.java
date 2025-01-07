package com.genio.controller;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.mapper.ConventionMapper;
import com.genio.service.GenioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class GenioController {

    private static final Logger logger = LoggerFactory.getLogger(GenioController.class);

    @Autowired
    private GenioService genioService;

    @PostMapping("/genio/generer")
    public ResponseEntity<ConventionBinaireRes> genererConvention(
            @Valid @RequestBody ConventionWsDTO input, // Validation automatique
            @RequestParam String formatFichierOutput) {

        logger.info("Requête reçue pour générer une convention avec format : {}", formatFichierOutput);

        try {
            // Mapper le DTO Web en DTO Service
            ConventionServiceDTO serviceInput = ConventionMapper.toServiceDTO(input);

            // Appeler le service avec le DTO mappé
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
}