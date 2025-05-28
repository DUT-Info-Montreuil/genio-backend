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