package com.genio.factory;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.Convention;
import com.genio.model.Etudiant;
import com.genio.model.Modele;
import com.genio.model.Tuteur;

public class ConventionFactory {

    public static Convention createConvention(ConventionServiceDTO input, Etudiant etudiant, Tuteur tuteur, Modele modele) {
        if (input == null || etudiant == null || tuteur == null || modele == null) {
            throw new IllegalArgumentException("Les données nécessaires pour créer une convention sont nulles.");
        }

        Convention convention = new Convention();
        convention.setAnnee(input.getAnnee());
        convention.setEtudiant(etudiant);
        convention.setTuteur(tuteur);
        convention.setModele(modele);

        return convention;
    }
}
