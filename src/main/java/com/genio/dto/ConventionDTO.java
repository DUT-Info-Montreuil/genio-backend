package com.genio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ConventionDTO {

    @NotBlank(message = "L'année de la convention est obligatoire.")
    @Pattern(regexp = "^\\d{4}$", message = "L'année doit être au format YYYY.")
    private String annee;

    @NotBlank(message = "La CPAM de la convention est obligatoire.")
    private String cpam;

    public ConventionDTO(String annee, String cpam) {
        this.annee = annee;
        this.cpam = cpam;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCpam() {
        return cpam;
    }
}