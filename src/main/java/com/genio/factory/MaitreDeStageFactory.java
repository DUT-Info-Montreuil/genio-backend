package com.genio.factory;

import com.genio.dto.MaitreDeStageDTO;
import com.genio.model.MaitreDeStage;

public class MaitreDeStageFactory {

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