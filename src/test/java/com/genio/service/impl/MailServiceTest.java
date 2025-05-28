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
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

package com.genio.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {

    private JavaMailSender mailSender;
    private MailService mailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        mailService = new MailService(mailSender);
    }

    @Test
    void sendResetPasswordEmail_shouldCallMailSenderSend() {
        String destinataire = "test@genio.com";
        String token = "abc123";

        mailService.sendResetPasswordEmail(destinataire, token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendResetPasswordEmail_shouldGenerateCorrectMessageContent() {
        String destinataire = "test@genio.com";
        String token = "token123";
        String expectedLink = "http://localhost:4200/reset-password?token=token123";

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        mailService.sendResetPasswordEmail(destinataire, token);

        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertEquals("genioservice3@gmail.com", message.getFrom());
        assertArrayEquals(new String[]{destinataire}, message.getTo());
        assertEquals("Réinitialisation de votre mot de passe GenioService", message.getSubject());
        assertTrue(message.getText().contains(expectedLink));
    }
}