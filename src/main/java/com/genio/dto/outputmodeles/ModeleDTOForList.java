package com.genio.dto.outputmodeles;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModeleDTOForList {

    private Long id;
    private String nom;
    private String description;
    private String format;
    private String titre;

    public ModeleDTOForList(Long id, String nom, String description, String format, String titre) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.format = format;
        this.titre=titre;
    }

    private String dateDerniereModification;

    @Override
    public String toString() {
        return "ModeleDTOForList{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", format='" + format + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }

}
