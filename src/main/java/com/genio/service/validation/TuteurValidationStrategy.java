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
            erreurs.put("tuteur", ErrorMessages.MISSING_TUTOR_NAME);
        } else {
            if (input.getTuteur().getNom() == null || input.getTuteur().getNom().isEmpty() || !input.getTuteur().getNom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("tuteur.nom", ErrorMessages.INVALID_TUTOR_NAME);
            }
            if (input.getTuteur().getPrenom() == null || input.getTuteur().getPrenom().isEmpty() || !input.getTuteur().getPrenom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("tuteur.prenom", ErrorMessages.INVALID_TUTOR_FIRST_NAME);
            }
            if (input.getTuteur().getEmail() == null || !input.getTuteur().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("tuteur.email", ErrorMessages.INVALID_TUTOR_EMAIL);
            }
            if (input.getTuteur().getTelephone() == null || !input.getTuteur().getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
                erreurs.put("tuteur.telephone", ErrorMessages.INVALID_TUTOR_PHONE);
            }
            if (input.getTuteur().getFonction() == null || input.getTuteur().getFonction().isEmpty()) {
                erreurs.put("tuteur.fonction", ErrorMessages.MISSING_TUTOR_FUNCTION);
            }
        }

        return erreurs;
    }
}