package com.genio.model;

import jakarta.persistence.*;

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
}