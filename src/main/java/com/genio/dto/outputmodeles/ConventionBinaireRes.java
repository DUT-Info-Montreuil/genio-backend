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