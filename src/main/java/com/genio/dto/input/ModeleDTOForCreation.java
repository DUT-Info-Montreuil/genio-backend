package com.genio.dto.input;

import java.util.List;

public class ModeleDTOForCreation {
    private String nom;
    private String description;
    private String contenu;
    private List<String> variables;


    public ModeleDTOForCreation(String nom, String description, String contenu, List<String> variables) {
        this.nom = nom;
        this.description = description;
        this.contenu = contenu;
        this.variables = variables;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }
}