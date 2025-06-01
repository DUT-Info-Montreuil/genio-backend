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

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.MaitreDeStageDTO;
import com.genio.config.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class MaitreDeStageValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();
        MaitreDeStageDTO mds = input.getMaitreDeStage();

        if (mds == null) {
            erreurs.put("maitreDeStage", ErrorMessages.MISSING_TUTOR_NAME);
            return erreurs;
        }

        validateNom(mds, erreurs);
        validatePrenom(mds, erreurs);
        validateEmail(mds, erreurs);
        validateTelephone(mds, erreurs);
        validateFonction(mds, erreurs);

        return erreurs;
    }

    private void validateNom(MaitreDeStageDTO mds, Map<String, String> erreurs) {
        if (isNullOrEmpty(mds.getNom()) || !mds.getNom().matches("^[a-zA-Z\\s]+$")) {
            erreurs.put("maitreDeStage.nom", ErrorMessages.INVALID_TUTOR_NAME);
        }
    }

    private void validatePrenom(MaitreDeStageDTO mds, Map<String, String> erreurs) {
        if (isNullOrEmpty(mds.getPrenom()) || !mds.getPrenom().matches("^[a-zA-Z\\s]+$")) {
            erreurs.put("maitreDeStage.prenom", ErrorMessages.INVALID_TUTOR_FIRST_NAME);
        }
    }

    private void validateEmail(MaitreDeStageDTO mds, Map<String, String> erreurs) {
        if (mds.getEmail() == null || !mds.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            erreurs.put("maitreDeStage.email", ErrorMessages.INVALID_TUTOR_EMAIL);
        }
    }

    private void validateTelephone(MaitreDeStageDTO mds, Map<String, String> erreurs) {
        if (mds.getTelephone() == null || !mds.getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
            erreurs.put("maitreDeStage.telephone", ErrorMessages.INVALID_TUTOR_PHONE);
        }
    }

    private void validateFonction(MaitreDeStageDTO mds, Map<String, String> erreurs) {
        if (isNullOrEmpty(mds.getFonction())) {
            erreurs.put("maitreDeStage.fonction", ErrorMessages.MISSING_TUTOR_FUNCTION);
        }
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}