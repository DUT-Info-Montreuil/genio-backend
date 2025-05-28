package com.genio.service.validation;

import com.genio.config.ErrorMessages;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.StageDTO;

import java.util.HashMap;
import java.util.Map;

public class StageValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();
        StageDTO stage = input.getStage();

        if (stage == null) {
            erreurs.put("stage", ErrorMessages.MISSING_STAGE_SUBJECT);
            return erreurs;
        }

        validateSujet(stage, erreurs);
        validateDateDebut(stage, erreurs);
        validateDateFin(stage, erreurs);
        validateDuree(stage, erreurs);
        validateRemuneration(stage, erreurs);
        validateAnnee(stage, erreurs);

        return erreurs;
    }

    private void validateSujet(StageDTO stage, Map<String, String> erreurs) {
        if (isEmpty(stage.getSujetDuStage())) {
            erreurs.put("stage.sujetDuStage", ErrorMessages.MISSING_STAGE_SUBJECT);
        }
    }

    private void validateDateDebut(StageDTO stage, Map<String, String> erreurs) {
        if (!matches(stage.getDateDebutStage(), "^\\d{4}-\\d{2}-\\d{2}$")) {
            erreurs.put("stage.dateDebutStage", ErrorMessages.INVALID_STAGE_START_DATE);
        }
    }

    private void validateDateFin(StageDTO stage, Map<String, String> erreurs) {
        if (!matches(stage.getDateFinStage(), "^\\d{4}-\\d{2}-\\d{2}$")) {
            erreurs.put("stage.dateFinStage", ErrorMessages.INVALID_STAGE_END_DATE);
        }
    }

    private void validateDuree(StageDTO stage, Map<String, String> erreurs) {
        if (isEmpty(stage.getDuree())) {
            erreurs.put("stage.duree", ErrorMessages.MISSING_STAGE_DURATION);
        }
    }

    private void validateRemuneration(StageDTO stage, Map<String, String> erreurs) {
        if (!matches(stage.getRemunerationHoraire(), "^\\d+(\\.\\d{1,2})?€$")) {
            erreurs.put("stage.remunerationHoraire", ErrorMessages.INVALID_STAGE_HOURLY_PAY);
        }
    }

    private void validateAnnee(StageDTO stage, Map<String, String> erreurs) {
        if (!matches(stage.getAnneeStage(), "^\\d{4}$")) {
            erreurs.put("stage.anneeStage", ErrorMessages.INVALID_CONVENTION_YEAR);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean matches(String value, String regex) {
        return value != null && value.matches(regex);
    }
}