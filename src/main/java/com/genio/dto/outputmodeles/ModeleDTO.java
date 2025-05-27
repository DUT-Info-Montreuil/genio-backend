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