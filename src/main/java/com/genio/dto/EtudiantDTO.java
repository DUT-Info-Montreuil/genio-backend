package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    public EtudiantDTO(String nom, String prenom, String sexe, String dateNaissance, String adresse, String telephone, String email, String cpam) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.cpam = cpam;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpam() {
        return cpam;
    }

    public void setCpam(String cpam) {
        this.cpam = cpam;
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

    public String getSexe() {
        return sexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }
}