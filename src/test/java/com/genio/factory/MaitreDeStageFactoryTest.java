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

package com.genio.factory;

import com.genio.dto.MaitreDeStageDTO;
import com.genio.model.MaitreDeStage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class MaitreDeStageFactoryTest {

    @Test
    void testCreateMaitreDeStage_shouldMapOnlyPersistedFields() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO(
                "Martin",
                "Paul",
                "Responsable IT",
                "01.23.45.67.89",
                "paul.martin@example.com"
        );

        MaitreDeStage maitre = MaitreDeStageFactory.createMaitreDeStage(dto);

        assertEquals("Martin", maitre.getNom());
        assertEquals("Paul", maitre.getPrenom());
        assertEquals("paul.martin@example.com", maitre.getEmail());
    }

    @Test
    void testCreateMaitreDeStage_shouldThrowException_whenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> MaitreDeStageFactory.createMaitreDeStage(null));
    }

    @Test
    void testPrivateConstructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<MaitreDeStageFactory> constructor = MaitreDeStageFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Cette classe utilitaire ne doit pas être instanciée.", cause.getMessage());
    }
}