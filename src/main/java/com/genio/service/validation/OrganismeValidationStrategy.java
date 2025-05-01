package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.config.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class OrganismeValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getOrganisme() == null) {
            erreurs.put("organisme", ErrorMessages.MISSING_ORGANIZATION_NAME);
        } else {
            if (input.getOrganisme().getNom() == null || input.getOrganisme().getNom().isEmpty()) {
                erreurs.put("organisme.nom", ErrorMessages.MISSING_ORGANIZATION_NAME);
            }
            if (input.getOrganisme().getAdresse() == null || input.getOrganisme().getAdresse().isEmpty()) {
                erreurs.put("organisme.adresse", ErrorMessages.MISSING_ORGANIZATION_ADDRESS);
            }
            if (input.getOrganisme().getNomRepresentant() == null || input.getOrganisme().getNomRepresentant().isEmpty()) {
                erreurs.put("organisme.nomRepresentant", ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_NAME);
            }
            if (input.getOrganisme().getQualiteRepresentant() == null || input.getOrganisme().getQualiteRepresentant().isEmpty()) {
                erreurs.put("organisme.qualiteRepresentant", ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_QUALITY);
            }
            if (input.getOrganisme().getNomDuService() == null || input.getOrganisme().getNomDuService().isEmpty()) {
                erreurs.put("organisme.nomDuService", ErrorMessages.MISSING_ORGANIZATION_SERVICE_NAME);
            }
            if (input.getOrganisme().getTelephone() == null || !input.getOrganisme().getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
                erreurs.put("organisme.telephone", ErrorMessages.INVALID_ORGANIZATION_PHONE);
            }
            if (input.getOrganisme().getEmail() == null || !input.getOrganisme().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("organisme.email", ErrorMessages.INVALID_ORGANIZATION_EMAIL);
            }
            if (input.getOrganisme().getLieuDuStage() == null || input.getOrganisme().getLieuDuStage().isEmpty()) {
                erreurs.put("organisme.lieuDuStage", ErrorMessages.MISSING_ORGANIZATION_STAGE_LOCATION);
            }
        }

        return erreurs;
    }
}