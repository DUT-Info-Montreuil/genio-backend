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

package com.genio.mapper;

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.input.ConventionWsDTO;

public class ConventionMapper {

    private ConventionMapper() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

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