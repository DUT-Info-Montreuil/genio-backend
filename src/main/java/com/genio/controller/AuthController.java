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

package com.genio.controller;

import com.genio.dto.LoginResponse;
import com.genio.exception.business.CompteInactifException;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.MailService;
import com.genio.service.impl.TokenService;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final TokenService tokenService;
    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private static final String MESSAGE_KEY = "message";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session
    ) {
        log.info("Tentative de connexion pour l'email : {}", email);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            log.info("Connexion réussie pour : {}", email);
            return ResponseEntity.ok(new LoginResponse("Connexion réussie"));

        } catch (BadCredentialsException ex) {
            log.warn("Échec de connexion - identifiants invalides pour : {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Email ou mot de passe incorrect."));

        } catch (InternalAuthenticationServiceException ex) {
            if (ex.getCause() instanceof CompteInactifException inactiveEx) {
                log.warn("Connexion refusée - compte inactif : {}", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse(inactiveEx.getMessage()));
            }
            log.error("Erreur interne d'authentification pour : {}", email, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Erreur interne d'authentification."));

        } catch (Exception e) {
            log.error("Erreur inattendue lors de la connexion pour : {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Erreur inattendue lors de l'authentification."));
        }
    }

    @PostMapping("/mot-de-passe-oublie")
    public ResponseEntity<Map<String, String>> motDePasseOublie(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {
            log.warn("Requête de mot de passe oublié sans email fourni.");
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(MESSAGE_KEY, "Email requis."));
        }

        log.info("Requête de mot de passe oublié reçue pour : {}", email.trim());

        utilisateurRepository.findByEmail(email.trim()).ifPresentOrElse(utilisateur -> {
            String token = tokenService.generateResetToken(utilisateur.getEmail());
            mailService.sendResetPasswordEmail(utilisateur.getEmail(), token);
            log.info("Email de réinitialisation envoyé à : {}", utilisateur.getEmail());
        }, () -> log.warn("Email non trouvé dans la base : {}", email.trim()));

        return ResponseEntity.ok(Collections.singletonMap(MESSAGE_KEY,
                "Si cet email est enregistré, un e-mail a été envoyé."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nouveauMotDePasse = request.get("nouveauMotDePasse");

        if (StringUtils.isBlank(token) || StringUtils.isBlank(nouveauMotDePasse)) {
            log.warn("Requête incomplète pour réinitialisation du mot de passe.");
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(MESSAGE_KEY, "Token et nouveau mot de passe requis."));
        }

        log.info("Réinitialisation du mot de passe avec token : {}", token);

        boolean result = tokenService.resetPassword(token, nouveauMotDePasse);
        if (result) {
            log.info("Mot de passe réinitialisé avec succès pour le token : {}", token);
            return ResponseEntity.ok(Collections.singletonMap(MESSAGE_KEY, "Mot de passe réinitialisé avec succès."));
        } else {
            log.warn("Échec de réinitialisation - token invalide ou expiré : {}", token);
            return ResponseEntity.status(400)
                    .body(Collections.singletonMap(MESSAGE_KEY, "Token invalide ou expiré."));
        }
    }
}