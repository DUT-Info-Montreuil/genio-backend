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

import com.genio.dto.EtudiantDTO;
import com.genio.model.Etudiant;

public class EtudiantFactory {

    private EtudiantFactory() {
        throw new UnsupportedOperationException("Cette classe ne doit pas être instanciée.");
    }

    public static Etudiant createEtudiant(EtudiantDTO etudiantDTO) {
        if (etudiantDTO == null) {
            throw new IllegalArgumentException("Les données de l'étudiant sont nulles.");
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setEmail(etudiantDTO.getEmail());
        etudiant.setPromotion(etudiantDTO.getPromotion());

        return etudiant;
    }
}