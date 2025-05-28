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

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;

@Builder
@Getter
public class OrganismeDTO {

    @Setter
    @NotBlank(message = "Le nom de l'organisme est obligatoire.")
    private String nom;

    @NotBlank(message = "L'adresse de l'organisme est obligatoire.")
    private String adresse;

    @NotBlank(message = "Le nom du représentant est obligatoire.")
    private String nomRepresentant;

    @NotBlank(message = "La qualité du représentant est obligatoire.")
    private String qualiteRepresentant;

    @NotBlank(message = "Le nom du service est obligatoire.")
    private String nomDuService;

    @NotBlank(message = "Le téléphone de l'organisme est obligatoire.")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$", message = "Le téléphone doit être au format XX.XX.XX.XX.XX.")
    private String telephone;

    @NotBlank(message = "L'email de l'organisme est obligatoire.")
    @Email(message = "L'email de l'organisme doit être valide.")
    private String email;

    @NotBlank(message = "Le lieu du stage est obligatoire.")
    private String lieuDuStage;


    @Override
    public String toString() {
        return "OrganismeDTO{" +
                "nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", nomRepresentant='" + nomRepresentant + '\'' +
                ", qualiteRepresentant='" + qualiteRepresentant + '\'' +
                ", nomDuService='" + nomDuService + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", lieuDuStage='" + lieuDuStage + '\'' +
                '}';
    }
}