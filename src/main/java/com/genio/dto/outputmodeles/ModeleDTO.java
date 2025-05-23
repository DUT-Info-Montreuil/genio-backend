package com.genio.dto.outputmodeles;

public class ModeleDTO {

    private Long id;
    private String nom;
    private String annee;
    private String format;
    private String dateCreation;

    private String titre;

    public ModeleDTO(Long id, String nom, String annee, String format, String dateCreation, String titre, String descriptionModification) {
        this.id = id;
        this.nom = nom;
        this.annee = annee;
        this.format = format;
        this.dateCreation = dateCreation;
        this.titre = titre;
        this.descriptionModification = descriptionModification;
    }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAnnee() {
        return annee;
    }

    public String getFormat() {
        return format;
    }


    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private String descriptionModification;

    public String getDescriptionModification() {
        return descriptionModification;
    }

    public void setDescriptionModification(String descriptionModification) {
        this.descriptionModification = descriptionModification;
    }

    @Override
    public String toString() {
        return "ModeleDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", annee='" + annee + '\'' +
                ", format='" + format + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}