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

package com.genio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String destinataire, String token) {
        logger.info("Préparation de l'envoi de l'e-mail de réinitialisation de mot de passe à {}", destinataire);
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

        try {
            mailSender.send(message);
            logger.info("E-mail de réinitialisation envoyé avec succès à {}", destinataire);
        } catch (Exception e) {
            logger.error("Échec de l'envoi de l'e-mail à {} : {}", destinataire, e.getMessage(), e);
        }
    }
}