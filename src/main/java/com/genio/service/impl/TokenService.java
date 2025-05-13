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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TokenService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
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