package com.genio.mapper;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;

public class ConventionMapper {

    public static ConventionServiceDTO toServiceDTO(ConventionWsDTO wsDTO) {
        if (wsDTO == null) {
            throw new IllegalArgumentException("Les données d'entrée sont nulles");
        }

        ConventionServiceDTO serviceDTO = new ConventionServiceDTO();
        serviceDTO.setEtudiant(wsDTO.getEtudiant());
        serviceDTO.setMaitreDeStage(wsDTO.getMaitreDeStage());
        serviceDTO.setOrganisme(wsDTO.getOrganisme());
        serviceDTO.setStage(wsDTO.getStage());
        serviceDTO.setTuteur(wsDTO.getTuteur());
        serviceDTO.setModeleId(wsDTO.getModeleId());

        return serviceDTO;
    }
}