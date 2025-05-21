package com.genio.dto.input;

import lombok.Data;

@Data
public class UtilisateurUpdateDTO {
    private String nom;
    private String prenom;
    private String role;
    private Boolean actif;
}