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