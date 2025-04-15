package com.genio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GenioServiceApplicationTests {

    @Test
    void testMainMethod_shouldNotCrash() {
        assertDoesNotThrow(() -> GenioServiceApplication.main(new String[]{}));
    }
}