package com.genio.dto;

import lombok.Data;

@Data
public class UtilisateurUpdateDTO {
    private String nom;
    private String prenom;
    private String role;
    private Boolean actif;

    @Override
    public String toString() {
        return "UtilisateurUpdateDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
}