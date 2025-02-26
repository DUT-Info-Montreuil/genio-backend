package com.genio.factory;

import com.genio.dto.TuteurDTO;
import com.genio.model.Tuteur;

// Dans TuteurFactory.java

public class TuteurFactory {

    public static Tuteur createTuteur(TuteurDTO tuteurDTO) {
        // Validation des données du TuteurDTO
        if (tuteurDTO == null) {
            throw new IllegalArgumentException("Les données du tuteur sont nulles.");
        }

        if (tuteurDTO.getNom() == null || tuteurDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du tuteur est requis.");
        }

        if (tuteurDTO.getEmail() == null || !tuteurDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("L'email du tuteur est invalide.");
        }

        // Création du tuteur à partir du DTO
        Tuteur tuteur = new Tuteur();
        tuteur.setNom(tuteurDTO.getNom());
        tuteur.setPrenom(tuteurDTO.getPrenom());
        tuteur.setEmail(tuteurDTO.getEmail());

        return tuteur;
    }
}