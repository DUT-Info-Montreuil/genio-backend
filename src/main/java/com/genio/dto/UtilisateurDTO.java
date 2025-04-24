package com.genio.dto;

import lombok.Data;

@Data
public class UtilisateurDTO {
    private String nom;
    private String prenom;
    private String username;
    private String motDePasse; // Non hashé, sera hashé côté service
}