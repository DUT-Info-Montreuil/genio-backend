package com.genio.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistorisationTest {

    @Test
    void testGettersAndSetters() {
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

    }
}