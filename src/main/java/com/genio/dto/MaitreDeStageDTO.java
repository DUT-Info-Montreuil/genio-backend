package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MaitreDeStageDTO {

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

    public void setNom(String nom) {
        this.nom = nom;
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
}