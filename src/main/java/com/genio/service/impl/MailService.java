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

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String destinataire, String token) {
        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("genioservice3@gmail.com");
        message.setTo(destinataire);
        message.setSubject("Réinitialisation de votre mot de passe GenioService");
        message.setText(
                "Bonjour,\n\n" +
                        "Vous avez demandé la réinitialisation de votre mot de passe.\n\n" +
                        "Voici votre lien sécurisé :\n" + resetLink + "\n\n" +
                        "Si vous n'avez pas effectué cette demande, veuillez ignorer cet e-mail.\n\n" +
                        "--\nGenioService");

        mailSender.send(message);
    }
}