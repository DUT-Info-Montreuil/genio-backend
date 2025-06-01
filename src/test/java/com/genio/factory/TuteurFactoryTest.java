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

package com.genio.factory;

import com.genio.dto.TuteurDTO;
import com.genio.model.Tuteur;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class TuteurFactoryTest {

    @Test
    void testCreateTuteur_shouldMapOnlyPersistedFields() {
        TuteurDTO dto = new TuteurDTO("Durand", "Luc", "luc.durand@example.com");

        Tuteur tuteur = TuteurFactory.createTuteur(dto);

        assertEquals("Durand", tuteur.getNom());
        assertEquals("Luc", tuteur.getPrenom());
        assertEquals("luc.durand@example.com", tuteur.getEmail());
    }

    @Test
    void testCreateTuteur_shouldThrowException_whenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> TuteurFactory.createTuteur(null));
    }
    @Test
    void testCreateTuteur_shouldThrowException_whenNomIsEmpty() {
        TuteurDTO dto = new TuteurDTO("", "Luc", "luc.durand@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> TuteurFactory.createTuteur(dto));
        assertEquals("Le nom du tuteur est requis.", exception.getMessage());
    }

    @Test
    void testCreateTuteur_shouldThrowException_whenEmailIsInvalid() {
        TuteurDTO dto = new TuteurDTO("Durand", "Luc", "email-invalide");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> TuteurFactory.createTuteur(dto));
        assertEquals("L'email du tuteur est invalide.", exception.getMessage());
    }

    @Test
    void testPrivateConstructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<TuteurFactory> constructor = TuteurFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });

        // Vérifie que la cause est UnsupportedOperationException avec le bon message
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Cette classe utilitaire ne doit pas être instanciée.", cause.getMessage());
    }


}