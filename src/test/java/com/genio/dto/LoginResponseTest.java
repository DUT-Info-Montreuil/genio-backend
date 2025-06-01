/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void testEmptyConstructor() {
        LoginResponse response = new LoginResponse();
        assertNull(response.getMessage(), "Message should be null after empty constructor");
    }

    @Test
    void testConstructorWithMessage() {
        String msg = "Connexion réussie";
        LoginResponse response = new LoginResponse(msg);
        assertEquals(msg, response.getMessage(), "Message should be set correctly");
    }

    @Test
    void testSetterAndGetter() {
        LoginResponse response = new LoginResponse();
        String msg = "Erreur de connexion";
        response.setMessage(msg);
        assertEquals(msg, response.getMessage(), "Getter should return the value set by setter");
    }
}