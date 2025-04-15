package com.genio.dto;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionBinaireResTest {

    @Test
    void testConstructorAndGetters() {
        byte[] fileData = new byte[]{1, 2, 3};

        ConventionBinaireRes res = new ConventionBinaireRes(true, fileData, null);

        assertTrue(res.isSuccess());
        assertArrayEquals(fileData, res.getFichierBinaire());
        assertNull(res.getMessageErreur());
    }

    @Test
    void testErrorCase() {
        ConventionBinaireRes res = new ConventionBinaireRes(false, null, "Erreur ici");

        assertFalse(res.isSuccess());
        assertNull(res.getFichierBinaire());
        assertEquals("Erreur ici", res.getMessageErreur());
    }


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