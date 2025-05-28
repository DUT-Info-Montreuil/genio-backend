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

import com.genio.config.ErrorMessages;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.EtudiantDTO;

import java.util.HashMap;
import java.util.Map;

public class EtudiantValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();
        EtudiantDTO etudiant = input.getEtudiant();

        if (etudiant == null) {
            erreurs.put("etudiant", ErrorMessages.MISSING_STUDENT_NAME);
            return erreurs;
        }

        validateNom(etudiant, erreurs);
        validatePrenom(etudiant, erreurs);
        validateEmail(etudiant, erreurs);
        validateAdresse(etudiant, erreurs);
        validateTelephone(etudiant, erreurs);
        validateSexe(etudiant, erreurs);
        validateDateNaissance(etudiant, erreurs);
        validateCpam(etudiant, erreurs);

        return erreurs;
    }

    private void validateNom(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (isNullOrEmpty(etudiant.getNom()) || !etudiant.getNom().matches("^[a-zA-Z\\s]+$")) {
            erreurs.put("etudiant.nom", ErrorMessages.INVALID_STUDENT_NAME);
        }
    }

    private void validatePrenom(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (isNullOrEmpty(etudiant.getPrenom()) || !etudiant.getPrenom().matches("^[a-zA-Z\\s]+$")) {
            erreurs.put("etudiant.prenom", ErrorMessages.INVALID_STUDENT_FIRST_NAME);
        }
    }

    private void validateEmail(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (etudiant.getEmail() == null || !etudiant.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            erreurs.put("etudiant.email", ErrorMessages.INVALID_STUDENT_EMAIL);
        }
    }

    private void validateAdresse(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (isNullOrEmpty(etudiant.getAdresse())) {
            erreurs.put("etudiant.adresse", ErrorMessages.MISSING_STUDENT_ADDRESS);
        }
    }

    private void validateTelephone(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (etudiant.getTelephone() == null || !etudiant.getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
            erreurs.put("etudiant.telephone", ErrorMessages.INVALID_STUDENT_PHONE);
        }
    }

    private void validateSexe(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (etudiant.getSexe() == null || !etudiant.getSexe().matches("^(H|F)$")) {
            erreurs.put("etudiant.sexe", ErrorMessages.INVALID_STUDENT_SEX);
        }
    }

    private void validateDateNaissance(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (etudiant.getDateNaissance() == null || !etudiant.getDateNaissance().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            erreurs.put("etudiant.dateNaissance", ErrorMessages.INVALID_STUDENT_DATE_OF_BIRTH);
        }
    }

    private void validateCpam(EtudiantDTO etudiant, Map<String, String> erreurs) {
        if (isNullOrEmpty(etudiant.getCpam())) {
            erreurs.put("etudiant.cpam", ErrorMessages.MISSING_CONVENTION_CPAM);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}