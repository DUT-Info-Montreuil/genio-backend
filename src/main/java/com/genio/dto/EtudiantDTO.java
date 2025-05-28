package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EtudiantDTO {

    @NotBlank(message = "Le nom de l'étudiant est obligatoire.")
    private String nom;

    @NotBlank(message = "Le prénom de l'étudiant est obligatoire.")
    private String prenom;

    @NotBlank(message = "Le sexe de l'étudiant est obligatoire.")
    @Pattern(regexp = "^(H|F)$", message = "Le sexe de l'étudiant doit être 'H' ou 'F'.")
    private String sexe;

    @NotBlank(message = "La date de naissance de l'étudiant est obligatoire.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La date de naissance doit être au format YYYY-MM-DD.")
    private String dateNaissance;

    @NotBlank(message = "L'adresse de l'étudiant est obligatoire.")
    private String adresse;

    @NotBlank(message = "Le téléphone de l'étudiant est obligatoire.")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{2}$", message = "Le téléphone doit être au format XX.XX.XX.XX.XX.")
    private String telephone;

    @NotBlank(message = "L'email de l'étudiant est obligatoire.")
    @Email(message = "L'email de l'étudiant doit être valide.")
    private String email;

    @NotBlank(message = "Le CPAM de l'étudiant est obligatoire.")
    private String cpam;

    @NotBlank(message = "La promotion de l'étudiant est obligatoire.")
    @Pattern(
            regexp = "^(?i)but ?[1-3]$",
            message = "La promotion doit être 'BUT' suivi de 1, 2 ou 3 (avec ou sans espace)."
    )
    private String promotion;

    @Override
    public String toString() {
        return "EtudiantDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", cpam='" + cpam + '\'' +
                ", promotion='" + promotion + '\'' +
                '}';
    }
}