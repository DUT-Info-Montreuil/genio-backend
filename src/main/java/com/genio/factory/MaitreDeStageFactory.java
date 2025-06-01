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

import com.genio.dto.MaitreDeStageDTO;
import com.genio.model.MaitreDeStage;

public class MaitreDeStageFactory {

    private MaitreDeStageFactory() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

    public static MaitreDeStage createMaitreDeStage(MaitreDeStageDTO maitreDeStageDTO) {
        if (maitreDeStageDTO == null) {
            throw new IllegalArgumentException("Les données du maitreDeStage sont nulles.");
        }

        MaitreDeStage maitreDeStage = new MaitreDeStage();
        maitreDeStage.setNom(maitreDeStageDTO.getNom());
        maitreDeStage.setPrenom(maitreDeStageDTO.getPrenom());
        maitreDeStage.setEmail(maitreDeStageDTO.getEmail());
        return maitreDeStage;
    }
}