package com.genio.model;

import jakarta.persistence.*;

@Entity
public class ErrorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String messageErreur;

    @Lob
    private String champsManquants;

    @ManyToOne
    @JoinColumn(name = "historisation_id", nullable = false)
    private Historisation historisation;

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public void setChampsManquants(String champsManquants) {
        this.champsManquants = champsManquants;
    }

    public void setHistorisation(Historisation historisation) {
        this.historisation = historisation;
    }

}