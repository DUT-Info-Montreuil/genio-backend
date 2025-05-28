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

package com.genio.service.validation;

import com.genio.config.ErrorMessages;
import com.genio.dto.MaitreDeStageDTO;
import com.genio.dto.input.ConventionServiceDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MaitreDeStageValidationStrategyTest {

    private final MaitreDeStageValidationStrategy strategy = new MaitreDeStageValidationStrategy();

    @Test
    void testValidate_shouldReturnErrorWhenMaitreIsNull() {
        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setMaitreDeStage(null);

        Map<String, String> errors = strategy.validate(dto);

        assertEquals(1, errors.size());
        assertEquals(ErrorMessages.MISSING_TUTOR_NAME, errors.get("maitreDeStage"));
    }

    @Test
    void testValidate_shouldReturnAllFieldErrors() {
        MaitreDeStageDTO mds = new MaitreDeStageDTO(
                "123",
                "!!!",
                "   ",
                "123456",
                "bad-email"
        );

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setMaitreDeStage(mds);

        Map<String, String> errors = strategy.validate(dto);

        assertEquals(5, errors.size());
        assertEquals(ErrorMessages.INVALID_TUTOR_NAME, errors.get("maitreDeStage.nom"));
        assertEquals(ErrorMessages.INVALID_TUTOR_FIRST_NAME, errors.get("maitreDeStage.prenom"));
        assertEquals(ErrorMessages.MISSING_TUTOR_FUNCTION, errors.get("maitreDeStage.fonction"));
        assertEquals(ErrorMessages.INVALID_TUTOR_PHONE, errors.get("maitreDeStage.telephone"));
        assertEquals(ErrorMessages.INVALID_TUTOR_EMAIL, errors.get("maitreDeStage.email"));
    }

    @Test
    void testValidate_shouldPassWithValidMaitre() {
        MaitreDeStageDTO mds = new MaitreDeStageDTO(
                "Martin",
                "Paul",
                "Responsable",
                "01.23.45.67.89",
                "paul.martin@example.com"
        );

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setMaitreDeStage(mds);

        Map<String, String> errors = strategy.validate(dto);

        assertTrue(errors.isEmpty());
    }
}