package com.genio.model;

import jakarta.persistence.*;

@Table(name = "convention")
@Entity
public class Convention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "maitreDeStage_id", nullable = false)
    private MaitreDeStage maitreDeStage;

    @ManyToOne
    @JoinColumn(name = "tuteur_id", nullable = false)
    private Tuteur tuteur;

    @ManyToOne
    @JoinColumn(name = "modele_id", nullable = false)
    private Modele modele;

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public void setMaitreDeStage(MaitreDeStage maitreDeStage) {
        this.maitreDeStage = maitreDeStage;
    }

    public void setModele(Modele modele) {
        this.modele = modele;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public MaitreDeStage getMaitreDeStage() {
        return maitreDeStage;
    }

    public Tuteur getTuteur() {
        return tuteur;
    }

    public Modele getModele() {
        return modele;
    }
}