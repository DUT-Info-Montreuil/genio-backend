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

package com.genio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDTO {

    @NotBlank(message = "Le sujet du stage est obligatoire.")
    private String sujetDuStage;

    @NotBlank(message = "La date de début est obligatoire.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La date de début doit être au format YYYY-MM-DD.")
    private String dateDebutStage;

    @NotBlank(message = "La date de fin est obligatoire.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La date de fin doit être au format YYYY-MM-DD.")
    private String dateFinStage;

    @NotBlank(message = "La durée est obligatoire.")
    private String duree;

    private Integer joursTot;
    private Integer heuresTot;

    @NotBlank(message = "La rémunération horaire est obligatoire.")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?€$", message = "La rémunération horaire doit être un montant valide suivi de '€'.")
    private String remunerationHoraire;

    @NotBlank(message = "L'indication du type de stage professionnel est obligatoire.")
    private String saeStageProfessionnel;

    @NotBlank(message = "L'année de la convention est obligatoire.")
    @Pattern(regexp = "^\\d{4}$", message = "L'année doit être au format YYYY.")
    private String anneeStage;

    @Override
    public String toString() {
        return "StageDTO{" +
                "sujetDuStage='" + sujetDuStage + '\'' +
                ", dateDebutStage='" + dateDebutStage + '\'' +
                ", dateFinStage='" + dateFinStage + '\'' +
                ", duree='" + duree + '\'' +
                ", joursTot=" + joursTot +
                ", heuresTot=" + heuresTot +
                ", remunerationHoraire='" + remunerationHoraire + '\'' +
                ", saeStageProfessionnel='" + saeStageProfessionnel + '\'' +
                ", anneeStage='" + anneeStage + '\'' +
                '}';
    }
}