package com.genio.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EnseignantDTO {

    @NotBlank(message = "Le nom de l'enseignant est obligatoire.")
    private String nom;

    @NotBlank(message = "Le prénom de l'enseignant est obligatoire.")
    private String prenom;

    @NotBlank(message = "L'email de l'enseignant est obligatoire.")
    @Email(message = "L'email de l'enseignant doit être valide.")
    private String email;

    public EnseignantDTO(String nom, String prenom, String email) {
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

}
