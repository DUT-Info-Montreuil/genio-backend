package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TuteurDTO {

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
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