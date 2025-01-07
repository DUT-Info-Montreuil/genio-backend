package com.genio.controller;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.mapper.ConventionMapper;
import com.genio.service.GenioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genio")
@Tag(name = "Convention Management", description = "Manage convention generation and operations")
@Validated
public class GenioController {

    private static final Logger logger = LoggerFactory.getLogger(GenioController.class);

    @Autowired
    private GenioService genioService;

    /**
     * Endpoint to generate a convention.
     * @param input The data for the convention.
     * @param formatFichierOutput The desired file format for the output.
     * @return A binary response containing the generated convention.
     */
    @PostMapping("/generer")
    @Operation(summary = "Generate a convention", description = "This endpoint generates a convention based on the input data.")
    public ResponseEntity<ConventionBinaireRes> genererConvention(
            @Parameter(description = "The convention data input", required = true) @Valid @RequestBody ConventionWsDTO input,
            @Parameter(description = "The file format for the generated convention") @RequestParam String formatFichierOutput) {

        logger.info("Requête reçue pour générer une convention avec format : {}", formatFichierOutput);

        try {
            // Mapping the input DTO to the service DTO
            ConventionServiceDTO serviceInput = ConventionMapper.toServiceDTO(input);

            // Calling the service to generate the convention
            ConventionBinaireRes result = genioService.generateConvention(serviceInput, formatFichierOutput);

            // Checking if the operation was successful
            if (result.isSuccess()) {
                logger.info("Convention générée avec succès.");
                return ResponseEntity.ok(result);  // Successful response
            } else {
                logger.warn("Erreur lors de la génération de la convention : {}", result.getMessageErreur());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);  // Error response
            }
        } catch (Exception e) {
            // Handle unexpected errors
            logger.error("Erreur inattendue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ConventionBinaireRes(false, null, "Erreur inattendue : " + e.getMessage()));  // Internal server error response
        }
    }
}