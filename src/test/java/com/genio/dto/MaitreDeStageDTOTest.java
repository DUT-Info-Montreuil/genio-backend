package com.genio.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import jakarta.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MaitreDeStageDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = (Validator) factory.getValidator();
    }

    @Test
    void testConstructorAndGetters() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("Martin", "Paul", "Chef de projet", "01.02.03.04.05", "paul.martin@example.com");

        assertEquals("Martin", dto.getNom());
        assertEquals("Paul", dto.getPrenom());
        assertEquals("Chef de projet", dto.getFonction());
        assertEquals("01.02.03.04.05", dto.getTelephone());
        assertEquals("paul.martin@example.com", dto.getEmail());
    }

    @Test
    void testSetNom() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("X", "Y", "Z", "01.01.01.01.01", "x@y.com");
        dto.setNom("Bernard");
        assertEquals("Bernard", dto.getNom());
    }

    @Test
    void testToString() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO("Nom", "Prénom", "Fonction", "02.03.04.05.06", "email@example.com");
        String result = dto.toString();
        assertTrue(result.contains("Nom"));
        assertTrue(result.contains("Prénom"));
        assertTrue(result.contains("Fonction"));
        assertTrue(result.contains("02.03.04.05.06"));
        assertTrue(result.contains("email@example.com"));
    }

    // Test validation constraints on setters

    @Test
    void testPrenomNotBlankValidation() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setPrenom(""); // empty string

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("prenom") &&
                v.getMessage().contains("Le prénom du tuteur est obligatoire"));
        assertTrue(found);
    }

    @Test
    void testFonctionNotBlankValidation() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setFonction(" "); // blank string

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fonction") &&
                v.getMessage().contains("La fonction du tuteur est obligatoire"));
        assertTrue(found);
    }

    @Test
    void testTelephonePatternValidation() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setTelephone("1234567890"); // invalid format

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telephone") &&
                v.getMessage().contains("Le téléphone doit être au format XX.XX.XX.XX.XX"));
        assertTrue(found);
    }

    @Test
    void testEmailValidation() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setEmail("not-an-email"); // invalid email

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                v.getMessage().contains("doit être valide"));
        assertTrue(found);
    }

    @Test
    void testValidTelephone() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setTelephone("01.23.45.67.89"); // valid format

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
        assertFalse(found);
    }

    @Test
    void testValidEmail() {
        MaitreDeStageDTO dto = new MaitreDeStageDTO();
        dto.setEmail("valid.email@example.com");

        Set<ConstraintViolation<MaitreDeStageDTO>> violations = validator.validate(dto);
        boolean found = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertFalse(found);
    }
}