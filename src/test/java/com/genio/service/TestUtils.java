package com.genio.service;

import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import com.genio.service.impl.ModeleService;

import java.util.UUID;

public class TestUtils {

    public static Modele createUniqueTestModele(ModeleService modeleService, ModeleRepository repository, String annee) {
        // 👉 Génère un contenu unique à chaque appel
        String uniqueContent = "template-" + UUID.randomUUID(); // 👍 identifiant unique
        byte[] content = uniqueContent.getBytes(); // 👍 contenu unique = hash unique

        Modele modele = new Modele();
        modele.setNom(uniqueContent + ".docx"); // nom unique
        modele.setAnnee(annee); // année passée en paramètre
        modele.setTitre("Titre auto");

        modele.setFichierBinaire(content); // contenu binaire
        modele.setFichierHash(modeleService.generateFileHash(content)); // hash généré à partir du contenu

        return repository.saveAndFlush(modele); // persisté et retourné
    }
}