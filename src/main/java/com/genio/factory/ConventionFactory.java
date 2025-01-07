package com.genio.factory;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.Convention;
import com.genio.model.Etudiant;
import com.genio.model.Modele;
import com.genio.model.MaitreDeStage;

public class ConventionFactory {

    public static Convention createConvention(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Modele modele) {
        if (input == null || etudiant == null || maitreDeStage == null || modele == null) {
            throw new IllegalArgumentException("Les données nécessaires pour créer une convention sont nulles.");
        }
        Convention convention = new Convention();
        convention.setEtudiant(etudiant);
        convention.setMaitreDeStage(maitreDeStage);
        convention.setModele(modele);

        return convention;
    }
}