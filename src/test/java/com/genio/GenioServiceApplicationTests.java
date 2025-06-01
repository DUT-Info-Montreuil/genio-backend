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

package com.genio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.genio.service.impl.HistorisationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class GenioServiceApplicationTests {

    @MockBean
    private HistorisationService historisationService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private GenioServiceApplication context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void testInitializeLogging() {
        assertDoesNotThrow(() -> GenioServiceApplication.initializeLogging());
    }
}