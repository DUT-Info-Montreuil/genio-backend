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
        serviceDTO.setTuteur(wsDTO.getTuteur());
        serviceDTO.setOrganisme(wsDTO.getOrganisme());
        serviceDTO.setStage(wsDTO.getStage());
        serviceDTO.setEnseignant(wsDTO.getEnseignant());
        serviceDTO.setConvention(wsDTO.getConvention());
        serviceDTO.setAnnee(wsDTO.getAnnee());
        serviceDTO.setModeleId(wsDTO.getModeleId());

        return serviceDTO;
    }
}