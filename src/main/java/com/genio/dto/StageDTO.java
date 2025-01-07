package com.genio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
    private String SaeStageProfessionnel;

    public StageDTO(String sujetDuStage, String dateDebutStage, String dateFinStage, String duree, Integer joursTot, Integer heuresTot, String remunerationHoraire, String saeStageProfessionnel) {
        this.sujetDuStage = sujetDuStage;
        this.dateDebutStage = dateDebutStage;
        this.dateFinStage = dateFinStage;
        this.duree = duree;
        this.joursTot = joursTot;
        this.heuresTot = heuresTot;
        this.remunerationHoraire = remunerationHoraire;
        SaeStageProfessionnel = saeStageProfessionnel;
    }

    public String getSaeStageProfessionnel() {
        return SaeStageProfessionnel;
    }

    public String getSujetDuStage() {
        return sujetDuStage;
    }

    public String getDateDebutStage() {
        return dateDebutStage;
    }

    public String getDateFinStage() {
        return dateFinStage;
    }

    public String getDuree() {
        return duree;
    }

    public Integer getJoursTot() {
        return joursTot;
    }

    public Integer getHeuresTot() {
        return heuresTot;
    }

    public String getRemunerationHoraire() {
        return remunerationHoraire;
    }
}