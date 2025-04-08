package com.genio.dto;

import com.genio.dto.outputmodeles.ConventionBinaireRes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionBinaireResTest {

    @Test
    void testConstructorAndGetters() {
        byte[] fileData = new byte[]{1, 2, 3};

        ConventionBinaireRes res = new ConventionBinaireRes(true, fileData, null);

        assertTrue(res.isSuccess()); // <- ici
        assertArrayEquals(fileData, res.getFichierBinaire());
        assertNull(res.getMessageErreur());
    }

    @Test
    void testErrorCase() {
        ConventionBinaireRes res = new ConventionBinaireRes(false, null, "Erreur ici");

        assertFalse(res.isSuccess()); // <- ici
        assertNull(res.getFichierBinaire());
        assertEquals("Erreur ici", res.getMessageErreur());
    }
}