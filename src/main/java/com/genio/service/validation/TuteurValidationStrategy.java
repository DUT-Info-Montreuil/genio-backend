package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

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