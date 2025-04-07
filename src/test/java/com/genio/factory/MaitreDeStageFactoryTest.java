package com.genio.factory;

import com.genio.dto.MaitreDeStageDTO;
import com.genio.model.MaitreDeStage;
import org.junit.jupiter.api.Test;

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
}