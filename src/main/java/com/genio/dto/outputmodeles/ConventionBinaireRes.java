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

package com.genio.dto.outputmodeles;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
public class ConventionBinaireRes {
    private byte[] fichierBinaire;
    private String messageErreur;
    private boolean success;

    public ConventionBinaireRes(boolean success, byte[] fichierBinaire, String messageErreur) {
        this.success = success;
        this.fichierBinaire = fichierBinaire;
        this.messageErreur = messageErreur;
    }

    @Override
    public String toString() {
        return "ConventionBinaireRes{" +
                "fichierBinaire=" + Arrays.toString(fichierBinaire) +
                ", messageErreur='" + messageErreur + '\'' +
                ", success=" + success +
                '}';
    }
}