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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrganismeDTOTest {

    @Test
    void testConstructorAndGetters() {
        OrganismeDTO dto = new OrganismeDTO(
                "Entreprise",
                "123 rue du test",
                "Mme Dupuis",
                "Directrice",
                "Service RH",
                "01.23.45.67.89",
                "contact@entreprise.com",
                "Paris"
        );

        assertEquals("Entreprise", dto.getNom());
        assertEquals("123 rue du test", dto.getAdresse());
        assertEquals("Mme Dupuis", dto.getNomRepresentant());
        assertEquals("Directrice", dto.getQualiteRepresentant());
        assertEquals("Service RH", dto.getNomDuService());
        assertEquals("01.23.45.67.89", dto.getTelephone());
        assertEquals("contact@entreprise.com", dto.getEmail());
        assertEquals("Paris", dto.getLieuDuStage());
    }

    @Test
    void testSetNom() {
        OrganismeDTO dto = new OrganismeDTO("X", "Y", "Z", "A", "B", "01.01.01.01.01", "x@y.com", "Paris");
        dto.setNom("NouveauNom");
        assertEquals("NouveauNom", dto.getNom());
    }

    @Test
    void testToString() {
        OrganismeDTO dto = new OrganismeDTO("Nom", "Adresse", "Représentant", "Qualité", "Service", "02.02.02.02.02", "mail@org.com", "Lyon");
        String result = dto.toString();
        assertTrue(result.contains("Nom"));
        assertTrue(result.contains("Adresse"));
        assertTrue(result.contains("mail@org.com"));
    }
}