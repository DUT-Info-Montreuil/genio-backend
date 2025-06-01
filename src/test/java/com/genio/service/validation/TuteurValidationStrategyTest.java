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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.service.validation;

import com.genio.config.ErrorMessages;
import com.genio.dto.TuteurDTO;
import com.genio.dto.input.ConventionServiceDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TuteurValidationStrategyTest {

    private final TuteurValidationStrategy strategy = new TuteurValidationStrategy();

    @Test
    void testTuteurNull() {
        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setTuteur(null);

        Map<String, String> errors = strategy.validate(dto);
        assertEquals(1, errors.size());
        assertEquals(ErrorMessages.MISSING_TEACHER_NAME, errors.get("tuteur"));
    }

    @Test
    void testInvalidNomPrenomEmail() {
        TuteurDTO tuteur = new TuteurDTO();
        tuteur.setNom("123");
        tuteur.setPrenom("");
        tuteur.setEmail("invalid-email");

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setTuteur(tuteur);

        Map<String, String> errors = strategy.validate(dto);
        assertEquals(3, errors.size());
        assertEquals(ErrorMessages.INVALID_TEACHER_NAME, errors.get("tuteur.nom"));
        assertEquals(ErrorMessages.INVALID_TEACHER_FIRST_NAME, errors.get("tuteur.prenom"));
        assertEquals(ErrorMessages.INVALID_TEACHER_EMAIL, errors.get("tuteur.email"));
    }

    @Test
    void testValidTuteur() {
        TuteurDTO tuteur = new TuteurDTO();
        tuteur.setNom("Martin");
        tuteur.setPrenom("Alice");
        tuteur.setEmail("alice.martin@iut.fr");

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setTuteur(tuteur);

        Map<String, String> errors = strategy.validate(dto);
        assertTrue(errors.isEmpty());
    }
}