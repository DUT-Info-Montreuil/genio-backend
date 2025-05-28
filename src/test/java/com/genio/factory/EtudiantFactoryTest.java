package com.genio.factory;

import com.genio.dto.EtudiantDTO;
import com.genio.model.Etudiant;
import org.junit.jupiter.api.Test;

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
}