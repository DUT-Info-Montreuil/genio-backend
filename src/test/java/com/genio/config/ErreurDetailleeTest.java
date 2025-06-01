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

package com.genio.config;

import com.genio.utils.ErreurType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErreurDetailleeTest {

    @Test
    void testGetters() {
        ErreurDetaillee erreur = new ErreurDetaillee("champTest", "messageTest", ErreurType.DOCX);

        assertEquals("champTest", erreur.getChamp());
        assertEquals("messageTest", erreur.getMessage());
        assertEquals(ErreurType.DOCX, erreur.getType());
    }

    @Test
    void testToString() {
        ErreurDetaillee erreur = new ErreurDetaillee("champTest", "messageTest", ErreurType.JSON);
        String expected = "[" + ErreurType.JSON + "] champTest : messageTest";

        assertEquals(expected, erreur.toString());
    }
}