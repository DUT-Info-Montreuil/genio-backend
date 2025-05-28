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

@Setter
@Getter
public class ModeleDTO {

    private Long id;
    private String nom;
    private String annee;
    private String format;
    private String dateCreation;

    private String titre;

    public ModeleDTO(Long id, String nom, String annee, String format, String dateCreation, String titre, String descriptionModification) {
        this.id = id;
        this.nom = nom;
        this.annee = annee;
        this.format = format;
        this.dateCreation = dateCreation;
        this.titre = titre;
        this.descriptionModification = descriptionModification;
    }

    private String dateDerniereModification;


    private String descriptionModification;

    @Override
    public String toString() {
        return "ModeleDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", annee='" + annee + '\'' +
                ", format='" + format + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}