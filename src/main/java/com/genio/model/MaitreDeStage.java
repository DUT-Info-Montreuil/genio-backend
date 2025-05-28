package com.genio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "maitre_de_stage")
public class MaitreDeStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du maître de stage est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom du maître de stage est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email du maître de stage est obligatoire")
    @Email(message = "L'email du maître de stage est invalide")
    private String email;
}