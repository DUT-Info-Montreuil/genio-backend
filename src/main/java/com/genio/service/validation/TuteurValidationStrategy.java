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

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.config.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class TuteurValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getTuteur() == null) {
            erreurs.put("tuteur", ErrorMessages.MISSING_TEACHER_NAME);
        } else {
            if (input.getTuteur().getNom() == null || input.getTuteur().getNom().isEmpty() || !input.getTuteur().getNom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("tuteur.nom", ErrorMessages.INVALID_TEACHER_NAME);
            }
            if (input.getTuteur().getPrenom() == null || input.getTuteur().getPrenom().isEmpty() || !input.getTuteur().getPrenom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("tuteur.prenom", ErrorMessages.INVALID_TEACHER_FIRST_NAME);
            }
            if (input.getTuteur().getEmail() == null || !input.getTuteur().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("tuteur.email", ErrorMessages.INVALID_TEACHER_EMAIL);
            }
        }

        return erreurs;
    }
}