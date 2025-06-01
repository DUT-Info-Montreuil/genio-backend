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

import com.genio.dto.UtilisateurDTO;
import com.genio.exception.business.EmailDejaUtiliseException;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private static final Logger log = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Utilisateur creerUtilisateur(UtilisateurDTO dto) {
        log.info("Tentative de création d’un utilisateur avec l’e-mail : {}", dto.getEmail());

        if (utilisateurRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Échec : un utilisateur existe déjà avec cet e-mail : {}", dto.getEmail());
            throw new EmailDejaUtiliseException("Un utilisateur avec cet e-mail existe déjà.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setRole("NONE");
        utilisateur.setActif(false);
        utilisateur.setCreatedAt(LocalDateTime.now());
        utilisateur.setUpdatedAt(LocalDateTime.now());

        Utilisateur saved = utilisateurRepository.save(utilisateur);
        log.info("Utilisateur créé avec succès : ID={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        log.debug("Récupération de tous les utilisateurs.");
        return utilisateurRepository.findAll();
    }

    public void supprimerUtilisateur(Long id) {
        log.warn("Suppression de l’utilisateur ID={}", id);
        utilisateurRepository.deleteById(id);
    }

    public Optional<Utilisateur> modifierUtilisateur(Long id, UtilisateurDTO dto) {
        log.info("Modification de l’utilisateur ID={}", id);

        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateur.setNom(dto.getNom());
            utilisateur.setPrenom(dto.getPrenom());
            utilisateur.setEmail(dto.getEmail());

            if (dto.getMotDePasse() != null
                    && !dto.getMotDePasse().isBlank()
                    && !passwordEncoder.matches(dto.getMotDePasse(), utilisateur.getMotDePasse())) {
                utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
                log.debug("Mot de passe mis à jour pour l’utilisateur ID={}", id);
            }

            utilisateur.setUpdatedAt(LocalDateTime.now());
            return utilisateurRepository.save(utilisateur);
        });
    }

    public Utilisateur modifierRoleEtActivation(Long id, String nouveauRole, boolean actif) {
        log.info("Mise à jour du rôle et activation de l’utilisateur ID={} → rôle={}, actif={}", id, nouveauRole, actif);

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setRole(nouveauRole);
        utilisateur.setActif(actif);
        utilisateur.setUpdatedAt(LocalDateTime.now());

        return utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> modifierRoleEtStatut(Long id, String role, Boolean actif) {
        log.info("Modification rôle/statut utilisateur ID={} → rôle={}, actif={}", id, role, actif);

        return utilisateurRepository.findById(id).map(utilisateur -> {
            if (role != null) utilisateur.setRole(role);
            if (actif != null) utilisateur.setActif(actif);
            utilisateur.setUpdatedAt(LocalDateTime.now());
            return utilisateurRepository.save(utilisateur);
        });
    }

    public Optional<Utilisateur> getByEmail(String email) {
        log.debug("Recherche d’un utilisateur par e-mail : {}", email);
        return utilisateurRepository.findByEmail(email);
    }

    public List<Utilisateur> getUtilisateursNonActifs() {
        log.debug("Filtrage des utilisateurs non actifs, hors rôles GESTIONNAIRE/EXPLOITANT/CONSULTANT");

        return utilisateurRepository.findAll().stream()
                .filter(u -> !"GESTIONNAIRE".equals(u.getRole())
                        && !"EXPLOITANT".equals(u.getRole())
                        && !"CONSULTANT".equals(u.getRole())
                        && !u.isActif())
                .toList();
    }
}