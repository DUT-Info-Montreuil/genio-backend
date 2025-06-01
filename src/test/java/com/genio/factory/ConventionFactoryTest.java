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

import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConventionFactoryTest {

    @Test
    void testCreateConvention_shouldReturnValidConvention() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Dupont");

        MaitreDeStage maitreDeStage = new MaitreDeStage();
        maitreDeStage.setNom("Martin");

        Tuteur tuteur = new Tuteur();
        tuteur.setNom("Durand");

        Modele modele = new Modele();
        modele.setNom("modeleConvention_2025.docx");

        ConventionServiceDTO input = new ConventionServiceDTO();

        Convention convention = ConventionFactory.createConvention(input, etudiant, maitreDeStage, tuteur, modele);

        assertNotNull(convention);
        assertEquals(etudiant, convention.getEtudiant());
        assertEquals(maitreDeStage, convention.getMaitreDeStage());
        assertEquals(tuteur, convention.getTuteur());
        assertEquals(modele, convention.getModele());
    }

    @Test
    void testCreateConvention_shouldThrowException_whenAnyArgIsNull() {
        ConventionServiceDTO input = new ConventionServiceDTO();
        Etudiant etudiant = new Etudiant();
        MaitreDeStage maitre = new MaitreDeStage();
        Tuteur tuteur = new Tuteur();
        Modele modele = new Modele();

        assertThrows(IllegalArgumentException.class, () -> ConventionFactory.createConvention(null, etudiant, maitre, tuteur, modele));
        assertThrows(IllegalArgumentException.class, () -> ConventionFactory.createConvention(input, null, maitre, tuteur, modele));
        assertThrows(IllegalArgumentException.class, () -> ConventionFactory.createConvention(input, etudiant, null, tuteur, modele));
        assertThrows(IllegalArgumentException.class, () -> ConventionFactory.createConvention(input, etudiant, maitre, null, modele));
        assertThrows(IllegalArgumentException.class, () -> ConventionFactory.createConvention(input, etudiant, maitre, tuteur, null));
    }

    @Test
    void testPrivateConstructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<ConventionFactory> constructor = ConventionFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });

        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Cette classe ne doit pas être instanciée.", cause.getMessage());
    }
}