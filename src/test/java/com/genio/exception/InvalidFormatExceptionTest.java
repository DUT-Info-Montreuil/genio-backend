package com.genio.exception;

import com.genio.exception.business.InvalidFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidFormatExceptionTest {

    @Test
    void testConstructor_setsMessageCorrectly() {
        InvalidFormatException ex = new InvalidFormatException("format invalide");
        assertEquals("format invalide", ex.getMessage());
    }
}