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

package com.genio.dto.input;

import com.genio.dto.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConventionServiceDTO {

    @NotNull
    private EtudiantDTO etudiant;

    @NotNull
    private MaitreDeStageDTO maitreDeStage;

    @NotNull
    private OrganismeDTO organisme;

    @NotNull
    private StageDTO stage;

    @NotNull
    private TuteurDTO tuteur;

    @NotNull
    private Long modeleId;

    @Override
    public String toString() {
        return "ConventionServiceDTO{" +
                "etudiant=" + etudiant +
                ", maitreDeStage=" + maitreDeStage +
                ", organisme=" + organisme +
                ", stage=" + stage +
                ", tuteur=" + tuteur +
                ", modeleId=" + modeleId +
                '}';
    }
}