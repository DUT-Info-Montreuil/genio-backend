package com.genio.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrganismeDTO {

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

    public OrganismeDTO(String nom, String adresse, String nomRepresentant, String qualiteRepresentant, String nomDuService, String telephone, String email, String lieuDuStage) {
        this.nom = nom;
        this.adresse = adresse;
        this.nomRepresentant = nomRepresentant;
        this.qualiteRepresentant = qualiteRepresentant;
        this.nomDuService = nomDuService;
        this.telephone = telephone;
        this.email = email;
        this.lieuDuStage = lieuDuStage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNomRepresentant() {
        return nomRepresentant;
    }

    public String getQualiteRepresentant() {
        return qualiteRepresentant;
    }

    public String getNomDuService() {
        return nomDuService;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getLieuDuStage() {
        return lieuDuStage;
    }
}