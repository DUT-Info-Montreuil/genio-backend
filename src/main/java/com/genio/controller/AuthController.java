package com.genio.controller;

import com.genio.dto.LoginResponse;
import com.genio.exception.business.CompteInactifException;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.MailService;
import com.genio.service.impl.TokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return ResponseEntity.ok(new LoginResponse("Connexion réussie"));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Identifiants invalides"));

        } catch (InternalAuthenticationServiceException e) {
            if (e.getCause() instanceof CompteInactifException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(e.getCause().getMessage()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Erreur interne d'authentification"));
        }
    }

    @PostMapping("/mot-de-passe-oublie")
    public ResponseEntity<?> motDePasseOublie(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email requis."));
        }

        utilisateurRepository.findByEmail(email.trim()).ifPresent(utilisateur -> {
            String token = tokenService.generateResetToken(utilisateur.getEmail());
            mailService.sendResetPasswordEmail(utilisateur.getEmail(), token);
        });

        return ResponseEntity.ok(Collections.singletonMap("message", "Si cet email est enregistré, un e-mail a été envoyé."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nouveauMotDePasse = request.get("nouveauMotDePasse");

        if (token == null || token.isEmpty() || nouveauMotDePasse == null || nouveauMotDePasse.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Token et nouveau mot de passe requis."));
        }

        boolean result = tokenService.resetPassword(token, nouveauMotDePasse);
        if (result) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Mot de passe réinitialisé avec succès."));
        } else {
            return ResponseEntity.status(400).body(Collections.singletonMap("message", "Token invalide ou expiré."));
        }
    }
}