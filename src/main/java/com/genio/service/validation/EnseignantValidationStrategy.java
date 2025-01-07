package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class EnseignantValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getEnseignant() == null) {
            erreurs.put("enseignant", ErrorMessages.MISSING_TEACHER_NAME);
        } else {
            if (input.getEnseignant().getNom() == null || input.getEnseignant().getNom().isEmpty() || !input.getEnseignant().getNom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("enseignant.nom", ErrorMessages.INVALID_TEACHER_NAME);
            }
            if (input.getEnseignant().getPrenom() == null || input.getEnseignant().getPrenom().isEmpty() || !input.getEnseignant().getPrenom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("enseignant.prenom", ErrorMessages.INVALID_TEACHER_FIRST_NAME);
            }
            if (input.getEnseignant().getEmail() == null || !input.getEnseignant().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("enseignant.email", ErrorMessages.INVALID_TEACHER_EMAIL);
            }
        }

        return erreurs;
    }
}