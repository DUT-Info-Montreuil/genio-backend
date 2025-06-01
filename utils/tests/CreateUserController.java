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

package com.genio;

import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class CreateUserController {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    public String createTestUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role
    ) {
        Utilisateur utilisateur = Utilisateur.builder()
                .email(email)
                .motDePasse(passwordEncoder.encode(password))
                .nom("Test")
                .prenom("User")
                .role(role)
                .actif(true)
                .build();

        utilisateurRepository.save(utilisateur);
        return "Utilisateur " + email + " créé avec succès.";
    }

    @PutMapping("/update-password")
    public String updatePassword(
            @RequestParam String email,
            @RequestParam String newPassword
    ) {
        return utilisateurRepository.findByEmail(email).map(user -> {
            user.setMotDePasse(passwordEncoder.encode(newPassword));
            utilisateurRepository.save(user);
            return "Mot de passe de " + email + " mis à jour.";
        }).orElse("Utilisateur " + email + " non trouvé.");
    }
}