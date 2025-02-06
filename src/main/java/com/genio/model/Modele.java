package com.genio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modele")
public class Modele {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)

    private String nom;
    @Column(nullable = false)
    private String annee;

    @Lob
    @Column(name = "fichier_binaire", columnDefinition = "LONGBLOB")
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
}