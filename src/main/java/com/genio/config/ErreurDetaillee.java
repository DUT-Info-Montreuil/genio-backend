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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.config;

import com.genio.utils.ErreurType;

public class ErreurDetaillee {
    private String champ;
    private String message;
    private ErreurType type;

    public ErreurDetaillee(String champ, String message, ErreurType type) {
        this.champ = champ;
        this.message = message;
        this.type = type;
    }

    public String getChamp() { return champ; }
    public String getMessage() { return message; }
    public ErreurType getType() { return type; }

    @Override
    public String toString() {
        return "[" + type + "] " + champ + " : " + message;
    }
}