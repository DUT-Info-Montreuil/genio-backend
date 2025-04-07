package com.genio.factory;

import com.genio.dto.TuteurDTO;
import com.genio.model.Tuteur;
import org.junit.jupiter.api.Test;

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
}