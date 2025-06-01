package com.genio.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessagesTest {

    @Test
    void testPrivateConstructorThrowsException() throws NoSuchMethodException {
        Constructor<ErrorMessages> constructor = ErrorMessages.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> constructor.newInstance());

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Classe utilitaire", cause.getMessage());
    }
}