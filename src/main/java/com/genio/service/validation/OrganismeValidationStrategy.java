package com.genio.service.validation;

import com.genio.config.ErrorMessages;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.OrganismeDTO;

import java.util.HashMap;
import java.util.Map;

public class OrganismeValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();
        OrganismeDTO org = input.getOrganisme();

        if (org == null) {
            erreurs.put("organisme", ErrorMessages.MISSING_ORGANIZATION_NAME);
            return erreurs;
        }

        checkNom(org, erreurs);
        checkAdresse(org, erreurs);
        checkNomRepresentant(org, erreurs);
        checkQualiteRepresentant(org, erreurs);
        checkNomDuService(org, erreurs);
        checkTelephone(org, erreurs);
        checkEmail(org, erreurs);
        checkLieuDuStage(org, erreurs);

        return erreurs;
    }

    private void checkNom(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getNom())) {
            erreurs.put("organisme.nom", ErrorMessages.MISSING_ORGANIZATION_NAME);
        }
    }

    private void checkAdresse(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getAdresse())) {
            erreurs.put("organisme.adresse", ErrorMessages.MISSING_ORGANIZATION_ADDRESS);
        }
    }

    private void checkNomRepresentant(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getNomRepresentant())) {
            erreurs.put("organisme.nomRepresentant", ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_NAME);
        }
    }

    private void checkQualiteRepresentant(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getQualiteRepresentant())) {
            erreurs.put("organisme.qualiteRepresentant", ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_QUALITY);
        }
    }

    private void checkNomDuService(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getNomDuService())) {
            erreurs.put("organisme.nomDuService", ErrorMessages.MISSING_ORGANIZATION_SERVICE_NAME);
        }
    }

    private void checkTelephone(OrganismeDTO org, Map<String, String> erreurs) {
        if (org.getTelephone() == null || !org.getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
            erreurs.put("organisme.telephone", ErrorMessages.INVALID_ORGANIZATION_PHONE);
        }
    }

    private void checkEmail(OrganismeDTO org, Map<String, String> erreurs) {
        if (org.getEmail() == null || !org.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            erreurs.put("organisme.email", ErrorMessages.INVALID_ORGANIZATION_EMAIL);
        }
    }

    private void checkLieuDuStage(OrganismeDTO org, Map<String, String> erreurs) {
        if (isEmpty(org.getLieuDuStage())) {
            erreurs.put("organisme.lieuDuStage", ErrorMessages.MISSING_ORGANIZATION_STAGE_LOCATION);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}