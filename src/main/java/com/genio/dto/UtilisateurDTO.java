package com.genio.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class UtilisateurDTO {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    @Override
    public String toString() {
        return "UtilisateurDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                '}';
    }
}