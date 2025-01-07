package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationContext {

    private final List<ValidationStrategy> strategies = new ArrayList<>();


    public void addStrategy(ValidationStrategy strategy) {
        strategies.add(strategy);
    }

    public Map<String, String> executeValidations(ConventionServiceDTO input) {
        Map<String, String> erreurs = new HashMap<>();
        for (ValidationStrategy strategy : strategies) {
            erreurs.putAll(strategy.validate(input));
        }
        return erreurs;
    }
}