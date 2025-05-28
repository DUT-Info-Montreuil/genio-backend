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