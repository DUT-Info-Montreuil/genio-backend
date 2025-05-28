package com.genio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "tuteur")
@Entity
public class Tuteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du tuteur est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom du tuteur est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email du tuteur est obligatoire")
    @Email(message = "L'email du tuteur est invalide")
    private String email;
}