package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class EtudiantValidationStrategy implements ValidationStrategy {

    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getEtudiant() == null) {
            erreurs.put("etudiant", ErrorMessages.MISSING_STUDENT_NAME);
        } else {
            if (input.getEtudiant().getNom() == null || input.getEtudiant().getNom().isEmpty() || !input.getEtudiant().getNom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("etudiant.nom", ErrorMessages.INVALID_STUDENT_NAME);
            }
            if (input.getEtudiant().getPrenom() == null || input.getEtudiant().getPrenom().isEmpty() || !input.getEtudiant().getPrenom().matches("^[a-zA-Z\\s]+$")) {
                erreurs.put("etudiant.prenom", ErrorMessages.INVALID_STUDENT_FIRST_NAME);
            }
            if (input.getEtudiant().getEmail() == null || !input.getEtudiant().getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                erreurs.put("etudiant.email", ErrorMessages.INVALID_STUDENT_EMAIL);
            }
            if (input.getEtudiant().getAdresse() == null || input.getEtudiant().getAdresse().isEmpty()) {
                erreurs.put("etudiant.adresse", ErrorMessages.MISSING_STUDENT_ADDRESS);
            }
            if (input.getEtudiant().getTelephone() == null || !input.getEtudiant().getTelephone().matches("^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$")) {
                erreurs.put("etudiant.telephone", ErrorMessages.INVALID_STUDENT_PHONE);
            }
            if (input.getEtudiant().getSexe() == null || !input.getEtudiant().getSexe().matches("^(H|F)$")) {
                erreurs.put("etudiant.sexe", ErrorMessages.INVALID_STUDENT_SEX);
            }
            if (input.getEtudiant().getDateNaissance() == null || !input.getEtudiant().getDateNaissance().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                erreurs.put("etudiant.dateNaissance", ErrorMessages.INVALID_STUDENT_DATE_OF_BIRTH);
            }
            if (input.getEtudiant().getCpam() == null || input.getEtudiant().getCpam().isEmpty()) {
                erreurs.put("etudiant.cpam", ErrorMessages.MISSING_CONVENTION_CPAM);
            }
        }

        return erreurs;
    }
}
