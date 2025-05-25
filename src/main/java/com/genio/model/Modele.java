package com.genio.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "modele")
public class Modele {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre; // Titre modifiable

    @Column(length = 1000)
    private String descriptionModification; // Description optionnelle

    private LocalDateTime archivedAt;

    @Column(nullable = false)
    private boolean archived = false;

    @Column(name = "date_derniere_modification")
    private LocalDateTime dateDerniereModification;

    public LocalDateTime getDateDerniereModification() {
        return dateDerniereModification;
    }

    public void setDateDerniereModification(LocalDateTime dateDerniereModification) {
        this.dateDerniereModification = dateDerniereModification;
    }

    public String getDescriptionModification() {
        return descriptionModification;
    }

    public void setDescriptionModification(String descriptionModification) {
        this.descriptionModification = descriptionModification;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Column(nullable = false)

    private String nom;
    @Column(nullable = false)
    private String annee;

    @Lob
    @Column(name = "fichier_binaire", columnDefinition = "LONGBLOB")
    private byte[] fichierBinaire;

    @Column(name = "fichier_hash", unique = true, nullable = false)
    private String fichierHash;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFichierBinaire() {
        return fichierBinaire;
    }

    public void setFichierBinaire(byte[] fichierBinaire) {
        this.fichierBinaire = fichierBinaire;
    }

    public String getFichierHash() {
        return fichierHash;
    }

    public void setFichierHash(String fichierHash) {
        this.fichierHash = fichierHash;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}