package com.genio.model;

import jakarta.persistence.*;

@Table(name = "modele")
@Entity
public class Modele {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String annee;

    @Lob
    private byte[] fichierBinaire;

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

    public byte[] getFichierBinaire() {
        return fichierBinaire;
    }

    public void setFichierBinaire(byte[] fichierBinaire) {
        this.fichierBinaire = fichierBinaire;
    }
}