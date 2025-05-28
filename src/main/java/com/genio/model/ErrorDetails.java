/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

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
    @Column(name = "champsManquants", nullable = false)
    private String champsManquants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public String getChampsManquants() {
        return champsManquants;
    }

    public Historisation getHistorisation() {
        return historisation;
    }

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