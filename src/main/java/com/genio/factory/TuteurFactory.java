package com.genio.factory;

import com.genio.dto.TuteurDTO;
import com.genio.model.Tuteur;

public class TuteurFactory {

    private TuteurFactory() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

    public static Tuteur createTuteur(TuteurDTO tuteurDTO) {
        if (tuteurDTO == null) {
            throw new IllegalArgumentException("Les données du tuteur sont nulles.");
        }

        if (tuteurDTO.getNom() == null || tuteurDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du tuteur est requis.");
        }

        if (tuteurDTO.getEmail() == null || !tuteurDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("L'email du tuteur est invalide.");
        }

        Tuteur tuteur = new Tuteur();
        tuteur.setNom(tuteurDTO.getNom());
        tuteur.setPrenom(tuteurDTO.getPrenom());
        tuteur.setEmail(tuteurDTO.getEmail());

        return tuteur;
    }
}