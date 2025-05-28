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

class ConventionTest {

    @Test
    void testConvention_settersAndGetters() {
        Convention convention = new Convention();

        Etudiant etudiant = new Etudiant();
        MaitreDeStage mds = new MaitreDeStage();
        Tuteur tuteur = new Tuteur();
        Modele modele = new Modele();

        convention.setId(1L);
        convention.setEtudiant(etudiant);
        convention.setMaitreDeStage(mds);
        convention.setTuteur(tuteur);
        convention.setModele(modele);

        assertEquals(1L, convention.getId());
        assertEquals(etudiant, convention.getEtudiant());
        assertEquals(mds, convention.getMaitreDeStage());
        assertEquals(tuteur, convention.getTuteur());
        assertEquals(modele, convention.getModele());
    }

    @Test
    void testConvention_defaultConstructor() {
        Convention convention = new Convention();

        assertNull(convention.getId());
        assertNull(convention.getEtudiant());
        assertNull(convention.getMaitreDeStage());
        assertNull(convention.getTuteur());
        assertNull(convention.getModele());
    }
}