package com.genio.factory;

import com.genio.dto.TuteurDTO;
import com.genio.model.Tuteur;

public class TuteurFactory {

    public static Tuteur createTuteur(TuteurDTO tuteurDTO) {
        if (tuteurDTO == null) {
            throw new IllegalArgumentException("Les données du tuteur sont nulles.");
        }

        Tuteur tuteur = new Tuteur();
        tuteur.setNom(tuteurDTO.getNom());
        tuteur.setPrenom(tuteurDTO.getPrenom());
        tuteur.setEmail(tuteurDTO.getEmail());
        return tuteur;
    }
}