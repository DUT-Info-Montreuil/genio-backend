package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class StageValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getStage() == null) {
            erreurs.put("stage", ErrorMessages.MISSING_STAGE_SUBJECT);
        } else {
            if (input.getStage().getSujetDuStage() == null || input.getStage().getSujetDuStage().isEmpty()) {
                erreurs.put("stage.sujetDuStage", ErrorMessages.MISSING_STAGE_SUBJECT);
            }
            if (input.getStage().getDateDebutStage() == null || !input.getStage().getDateDebutStage().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                erreurs.put("stage.dateDebutStage", ErrorMessages.INVALID_STAGE_START_DATE);
            }
            if (input.getStage().getDateFinStage() == null || !input.getStage().getDateFinStage().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                erreurs.put("stage.dateFinStage", ErrorMessages.INVALID_STAGE_END_DATE);
            }
            if (input.getStage().getDuree() == null || input.getStage().getDuree().isEmpty()) {
                erreurs.put("stage.duree", ErrorMessages.MISSING_STAGE_DURATION);
            }
            if (input.getStage().getRemunerationHoraire() == null || !input.getStage().getRemunerationHoraire().matches("^\\d+(\\.\\d{1,2})?€$")) {
                erreurs.put("stage.remunerationHoraire", ErrorMessages.INVALID_STAGE_HOURLY_PAY);
            }
        }

        return erreurs;
    }
}