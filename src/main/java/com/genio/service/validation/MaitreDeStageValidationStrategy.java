package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class MaitreDeStageValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getMaitreDeStage() == null) {
            erreurs.put("maitreDeStage", ErrorMessages.MISSING_TUTOR_NAME);
        } else {
            if (input.getMaitreDeStage().getNom() == null || input.getMaitreDeStage().getNom().isEmpty() || !input.getMaitreDeStage().getNom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("maitreDeStage.nom", ErrorMessages.INVALID_TUTOR_NAME);
            }
            if (input.getMaitreDeStage().getPrenom() == null || input.getMaitreDeStage().getPrenom().isEmpty() || !input.getMaitreDeStage().getPrenom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("maitreDeStage.prenom", ErrorMessages.INVALID_TUTOR_FIRST_NAME);
            }
            if (input.getMaitreDeStage().getEmail() == null || !input.getMaitreDeStage().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("maitreDeStage.email", ErrorMessages.INVALID_TUTOR_EMAIL);
            }
            if (input.getMaitreDeStage().getTelephone() == null || !input.getMaitreDeStage().getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
                erreurs.put("maitreDeStage.telephone", ErrorMessages.INVALID_TUTOR_PHONE);
            }
            if (input.getMaitreDeStage().getFonction() == null || input.getMaitreDeStage().getFonction().isEmpty()) {
                erreurs.put("maitreDeStage.fonction", ErrorMessages.MISSING_TUTOR_FUNCTION);
            }
        }

        return erreurs;
    }
}