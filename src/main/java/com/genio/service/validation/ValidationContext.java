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