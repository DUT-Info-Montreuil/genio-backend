package com.genio.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailsTest {

    @Test
    void testGettersAndSetters() {
        ErrorDetails error = new ErrorDetails();
        error.setId(1L);
        error.setMessageErreur("Erreur test");
        error.setChampsManquants("champ1 ; champ2");

        Historisation histo = new Historisation();
        error.setHistorisation(histo);

        assertEquals(1L, error.getId());
        assertEquals("Erreur test", error.getMessageErreur());
        assertEquals("champ1 ; champ2", error.getChampsManquants());
        assertEquals(histo, error.getHistorisation());
    }
}