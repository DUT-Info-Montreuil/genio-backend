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

import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final UtilisateurRepository utilisateurRepository;
    private final Map<String, String> tokenStore = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenService(UtilisateurRepository utilisateurRepository,
                        BCryptPasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateResetToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);
        return token;
    }

    public boolean resetPassword(String token, String nouveauMotDePasse) {
        String email = tokenStore.get(token);
        if (email == null) {
            return false;
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
            utilisateurRepository.save(utilisateur);
            tokenStore.remove(token);
            return true;
        } else {
            return false;
        }
    }
}