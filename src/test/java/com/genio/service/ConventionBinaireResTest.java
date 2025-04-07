package com.genio.service;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionBinaireResTest {

    @Test
    void testSuccessConstructor_shouldSetFieldsCorrectly() {
        byte[] dummyFile = new byte[]{1, 2, 3};
        ConventionBinaireRes res = new ConventionBinaireRes(true, dummyFile, null);

        assertTrue(res.isSuccess());
        assertArrayEquals(dummyFile, res.getFichierBinaire());
        assertNull(res.getMessageErreur());
    }

    @Test
    void testFailureConstructor_shouldSetErrorMessage() {
        ConventionBinaireRes res = new ConventionBinaireRes(false, null, "Erreur test");

        assertFalse(res.isSuccess());
        assertNull(res.getFichierBinaire());
        assertEquals("Erreur test", res.getMessageErreur());
    }
}