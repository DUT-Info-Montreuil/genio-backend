package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TuteurDTO {

    @Setter
    @NotBlank(message = "Le nom du tuteur est obligatoire.")
    private String nom;

    @NotBlank(message = "Le prénom du tuteur est obligatoire.")
    private String prenom;

    @NotBlank(message = "L'email du tuteur est obligatoire.")
    @Email(message = "L'email du tuteur doit être valide.")
    private String email;

    public TuteurDTO(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    @Override
    public String toString() {
        return "TuteurDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}