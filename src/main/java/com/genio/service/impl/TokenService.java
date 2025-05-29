package com.genio.service.impl;

import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final UtilisateurRepository utilisateurRepository;
    private final Map<String, String> tokenStore = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenService(UtilisateurRepository utilisateurRepository,
                        BCryptPasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateResetToken(String email) {
        logger.info("Génération d'un token de réinitialisation pour l'e-mail : {}", email);
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);
        logger.debug("Token généré : {}", token);
        return token;
    }

    public boolean resetPassword(String token, String nouveauMotDePasse) {
        logger.info("Tentative de réinitialisation du mot de passe avec le token : {}", token);

        String email = tokenStore.get(token);
        if (email == null) {
            logger.warn("Échec : aucun email associé au token fourni.");
            return false;
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
            utilisateurRepository.save(utilisateur);
            tokenStore.remove(token);

            logger.info("Mot de passe réinitialisé avec succès pour l'utilisateur : {}", email);
            return true;
        } else {
            logger.error("Utilisateur introuvable avec l'e-mail associé au token : {}", email);
            return false;
        }
    }
}