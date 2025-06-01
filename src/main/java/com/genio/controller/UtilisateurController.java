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

package com.genio.controller;

import com.genio.dto.UtilisateurDTO;
import com.genio.dto.UtilisateurUpdateDTO;
import com.genio.exception.business.EmailDejaUtiliseException;
import com.genio.model.Utilisateur;
import com.genio.service.impl.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UtilisateurController.class);

    @PostMapping
    public ResponseEntity<Map<String, String>> creer(@RequestBody UtilisateurDTO dto) {
        logger.info("Tentative de création d'un nouvel utilisateur avec email : {}", dto.getEmail());
        Map<String, String> response = new HashMap<>();
        try {
            utilisateurService.creerUtilisateur(dto);
            logger.info("Utilisateur créé avec succès : {}", dto.getEmail());
            response.put("message", "Utilisateur créé avec succès.");
            return ResponseEntity.status(201).body(response);
        } catch (EmailDejaUtiliseException e) {
            logger.warn("Email déjà utilisé : {}", dto.getEmail());
            response.put("error", e.getMessage());
            return ResponseEntity.status(409).body(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur : {}", e.getMessage(), e);
            response.put("error", "Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        logger.info("Récupération de tous les utilisateurs.");
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        logger.info("Nombre d'utilisateurs récupérés : {}", utilisateurs.size());
        return ResponseEntity.ok(utilisateurs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Suppression de l'utilisateur avec ID : {}", id);
        utilisateurService.supprimerUtilisateur(id);
        logger.info("Utilisateur supprimé avec succès.");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        logger.info("Mise à jour de l'utilisateur ID {} avec l'email : {}", id, dto.getEmail());
        return utilisateurService.modifierUtilisateur(id, dto)
                .map(utilisateur -> {
                    logger.info("Utilisateur mis à jour avec succès.");
                    return ResponseEntity.ok(utilisateur);
                })
                .orElseGet(() -> {
                    logger.warn("Utilisateur non trouvé pour ID : {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}/role-activation")
    public ResponseEntity<Utilisateur> modifierRoleEtActivation(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam boolean actif
    ) {
        logger.info("Modification du rôle/activation pour utilisateur ID {}, nouveau rôle : {}, actif : {}", id, role, actif);
        Utilisateur utilisateur = utilisateurService.modifierRoleEtActivation(id, role, actif);
        logger.info("Modification appliquée avec succès pour ID {}", id);
        return ResponseEntity.ok(utilisateur);
    }


    @PutMapping("/{id}/admin-update")
    public ResponseEntity<Utilisateur> adminUpdate(
            @PathVariable Long id,
            @RequestBody UtilisateurUpdateDTO dto
    ) {
        logger.info("Mise à jour admin de l'utilisateur ID {} : nouveau rôle = {}, actif = {}", id, dto.getRole(), dto.getActif());
        return utilisateurService.modifierRoleEtStatut(id, dto.getRole(), dto.getActif())
                .map(utilisateur -> {
                    logger.info("Mise à jour admin réussie pour l'utilisateur ID {}", id);
                    return ResponseEntity.ok(utilisateur);
                })
                .orElseGet(() -> {
                    logger.warn("Utilisateur non trouvé pour mise à jour admin ID {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getUtilisateurConnecte(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Récupération des infos utilisateur connecté : {}", userDetails.getUsername());
        return utilisateurService.getByEmail(userDetails.getUsername())
                .map(utilisateur -> {
                    logger.info("Utilisateur connecté trouvé : {}", utilisateur.getEmail());
                    return ResponseEntity.ok(utilisateur);
                })
                .orElseGet(() -> {
                    logger.warn("Utilisateur connecté introuvable : {}", userDetails.getUsername());
                    return ResponseEntity.notFound().build();
                });
    }


    @GetMapping("/non-actifs")
    public ResponseEntity<List<Utilisateur>> getUtilisateursNonActifs() {
        logger.info("Récupération des utilisateurs non actifs.");
        List<Utilisateur> nonActifs = utilisateurService.getUtilisateursNonActifs();
        logger.info("Nombre d'utilisateurs non actifs : {}", nonActifs.size());
        return ResponseEntity.ok(nonActifs);
    }
}