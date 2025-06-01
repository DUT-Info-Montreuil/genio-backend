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

package com.genio.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class HistorisationTest {

    @Test
    void testSetTimestampWithoutParam() {
        Historisation histo = new Historisation();
        histo.setId(10L);
        histo.setStatus("SUCCES");
        histo.setDetails("Aucune erreur");
        histo.setDocxBinaire(new byte[]{1});
        histo.setPdfBinaire(new byte[]{2});
        histo.setFluxJsonBinaire(new byte[]{3});

        Convention convention = new Convention();
        histo.setConvention(convention);
        histo.setTimestamp();

        assertEquals(10L, histo.getId());
        assertEquals("SUCCES", histo.getStatus());
        assertEquals("Aucune erreur", histo.getDetails());
        assertArrayEquals(new byte[]{1}, histo.getDocxBinaire());
        assertArrayEquals(new byte[]{2}, histo.getPdfBinaire());
        assertArrayEquals(new byte[]{3}, histo.getFluxJsonBinaire());
        assertEquals(convention, histo.getConvention());
        assertNotNull(histo.getTimestamp());
    }

    @Test
    void testSetTimestampWithParam() {
        Historisation histo = new Historisation();

        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        histo.setTimestamp(date);

        assertEquals(date, histo.getTimestamp());
    }
}