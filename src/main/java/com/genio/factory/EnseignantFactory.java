package com.genio.factory;

import com.genio.dto.EnseignantDTO;
import com.genio.model.Enseignant;

public class EnseignantFactory {

    public static Enseignant createEnseignant(EnseignantDTO enseignantDTO) {
        if (enseignantDTO == null) {
            throw new IllegalArgumentException("Les données de l'enseignant sont nulles.");
        }

        Enseignant enseignant = new Enseignant();
        enseignant.setNom(enseignantDTO.getNom());
        enseignant.setPrenom(enseignantDTO.getPrenom());
        enseignant.setEmail(enseignantDTO.getEmail());
        return enseignant;
    }
}