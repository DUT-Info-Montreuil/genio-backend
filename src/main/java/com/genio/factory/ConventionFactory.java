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

package com.genio.factory;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.*;

public class ConventionFactory {

    private ConventionFactory() {
        throw new UnsupportedOperationException("Cette classe ne doit pas être instanciée.");
    }

    public static Convention createConvention(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Tuteur tuteur, Modele modele) {
        if (input == null || etudiant == null || maitreDeStage == null || tuteur == null || modele == null) {
            throw new IllegalArgumentException("Les données nécessaires pour créer une convention sont nulles.");
        }
        Convention convention = new Convention();
        convention.setEtudiant(etudiant);
        convention.setMaitreDeStage(maitreDeStage);
        convention.setTuteur(tuteur);
        convention.setModele(modele);

        return convention;
    }
}