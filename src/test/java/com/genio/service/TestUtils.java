/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

package com.genio.service;

import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.ModeleService;

import java.util.UUID;

public class TestUtils {

    public static Modele createUniqueTestModele(ModeleService modeleService, ModeleRepository repository, String annee) {
        String uniqueContent = "template-" + UUID.randomUUID();
        byte[] content = uniqueContent.getBytes();

        Modele modele = new Modele();
        modele.setNom(uniqueContent + ".docx");
        modele.setAnnee(annee);
        modele.setTitre("Titre auto");

        modele.setFichierBinaire(content);
        modele.setFichierHash(modeleService.generateFileHash(content));

        return repository.saveAndFlush(modele);
    }
}