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
}