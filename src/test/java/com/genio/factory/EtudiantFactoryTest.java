package com.genio.factory;

import com.genio.dto.EtudiantDTO;
import com.genio.model.Etudiant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantFactoryTest {

    @Test
    void testCreateEtudiant_shouldMapOnlyPersistedFields() {
        EtudiantDTO dto = new EtudiantDTO(
                "Doe",
                "John",
                "H",
                "2000-01-01",
                "123 rue Test",
                "01.23.45.67.89",
                "john.doe@example.com",
                "CPAM-Paris"
        );

        Etudiant etudiant = EtudiantFactory.createEtudiant(dto);

        assertEquals("John", etudiant.getPrenom());
        assertEquals("Doe", etudiant.getNom());
        assertEquals("john.doe@example.com", etudiant.getEmail());
    }

    @Test
    void testCreateEtudiant_shouldThrowException_whenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> EtudiantFactory.createEtudiant(null));
    }
}