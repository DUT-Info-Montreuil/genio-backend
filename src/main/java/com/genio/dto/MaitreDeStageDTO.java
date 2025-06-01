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
import lombok.Setter;

@Getter
public class MaitreDeStageDTO {

    @Setter
    @NotBlank(message = "Le nom du tuteur est obligatoire.")
    private String nom;

    @NotBlank(message = "Le prénom du tuteur est obligatoire.")
    private String prenom;

    @NotBlank(message = "La fonction du tuteur est obligatoire.")
    private String fonction;

    @NotBlank(message = "Le téléphone du tuteur est obligatoire.")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$", message = "Le téléphone doit être au format XX.XX.XX.XX.XX.")
    private String telephone;

    @NotBlank(message = "L'email du tuteur est obligatoire.")
    @Email(message = "L'email du tuteur doit être valide.")
    private String email;

    public MaitreDeStageDTO(String nom, String prenom, String fonction, String telephone, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.fonction = fonction;
        this.telephone = telephone;
        this.email = email;
    }

    public MaitreDeStageDTO() {
    }

    @Override
    public String toString() {
        return "MaitreDeStageDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", fonction='" + fonction + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setPrenom(@NotBlank(message = "Le prénom du tuteur est obligatoire.") String prenom) {
        this.prenom = prenom;
    }

    public void setFonction(@NotBlank(message = "La fonction du tuteur est obligatoire.") String fonction) {
        this.fonction = fonction;
    }

    public void setTelephone(@NotBlank(message = "Le téléphone du tuteur est obligatoire.") @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$", message = "Le téléphone doit être au format XX.XX.XX.XX.XX.") String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(@NotBlank(message = "L'email du tuteur est obligatoire.") @Email(message = "L'email du tuteur doit être valide.") String email) {
        this.email = email;
    }
}