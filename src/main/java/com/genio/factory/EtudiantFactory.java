package com.genio.factory;

import com.genio.dto.EtudiantDTO;
import com.genio.model.Etudiant;

public class EtudiantFactory {

    public static Etudiant createEtudiant(EtudiantDTO etudiantDTO) {
        if (etudiantDTO == null) {
            throw new IllegalArgumentException("Les données de l'étudiant sont nulles.");
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setEmail(etudiantDTO.getEmail());
        etudiant.setPromotion(etudiantDTO.getPromotion());

        return etudiant;
    }
}