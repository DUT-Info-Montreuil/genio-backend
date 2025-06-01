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

import com.genio.dto.EtudiantDTO;
import com.genio.model.Etudiant;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantFactoryTest {

    @Test
    void testCreateEtudiant_shouldMapOnlyPersistedFields() {
        EtudiantDTO dto = EtudiantDTO.builder()
                .nom("Doe")
                .prenom("John")
                .sexe("H")
                .dateNaissance("2000-01-01")
                .adresse("123 rue Exemple")
                .telephone("01.23.45.67.89")
                .email("john.doe@example.com")
                .cpam("CPAM123")
                .promotion("BUT2")
                .build();

        Etudiant etudiant = EtudiantFactory.createEtudiant(dto);

        assertEquals("John", etudiant.getPrenom());
        assertEquals("Doe", etudiant.getNom());
        assertEquals("john.doe@example.com", etudiant.getEmail());
    }

    @Test
    void testCreateEtudiant_shouldThrowException_whenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> EtudiantFactory.createEtudiant(null));
    }


    @Test
    void testPrivateConstructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<EtudiantFactory> constructor = EtudiantFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Cette classe ne doit pas être instanciée.", cause.getMessage());
    }
}