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