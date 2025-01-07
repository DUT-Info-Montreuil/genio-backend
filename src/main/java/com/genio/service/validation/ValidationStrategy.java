package com.genio.service.validation;

import com.genio.dto.input.ConventionServiceDTO;
import java.util.Map;

public interface ValidationStrategy {
    /**
     * Méthode pour valider une partie des données.
     *
     * @param input Données d'entrée à valider.
     * @return Une map contenant les erreurs, avec le champ comme clé et l'erreur comme valeur.
     */
    Map<String, String> validate(ConventionServiceDTO input);
}