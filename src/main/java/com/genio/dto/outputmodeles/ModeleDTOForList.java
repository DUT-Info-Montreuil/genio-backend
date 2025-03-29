package com.genio.dto.outputmodeles;

public class ModeleDTOForList {

    private Long id;
    private String nom;
    private String description;
    private String format;

    public ModeleDTOForList(Long id, String nom, String description, String format) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getFormat() {
        return format;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "ModeleDTOForList{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
