package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class ConventionValidationStrategy implements ValidationStrategy {
    @Override
    public Map<String, String> validate(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();

        if (input.getConvention() == null) {
            erreurs.put("convention", ErrorMessages.MISSING_CONVENTION_YEAR);
        } else {
            if (input.getConvention().getAnnee() == null || !input.getConvention().getAnnee().matches("^\\d{4}$")) {
                erreurs.put("convention.annee", ErrorMessages.INVALID_CONVENTION_YEAR);
            }
            if (input.getConvention().getCpam() == null || input.getConvention().getCpam().isEmpty()) {
                erreurs.put("convention.cpam", ErrorMessages.MISSING_CONVENTION_CPAM);
            }
        }
        return erreurs;
    }

}


