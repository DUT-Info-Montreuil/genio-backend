package com.genio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TuteurDTO {

    @NotBlank(message = "Le nom du tuteur est obligatoire.")
    private String nom;

    @NotBlank(message = "Le prénom du tuteur est obligatoire.")
    private String prenom;

    @NotBlank(message = "L'email du tuteur est obligatoire.")
    @Email(message = "L'email du tuteur doit être valide.")
    private String email;

    @Override
    public String toString() {
        return "TuteurDTO{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}