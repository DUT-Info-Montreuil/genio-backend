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

    @PostMapping
    public ResponseEntity<?> creer(@RequestBody UtilisateurDTO dto) {
        try {
            Utilisateur u = utilisateurService.creerUtilisateur(dto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur créé avec succès.");
            return ResponseEntity.status(201).body(response);
        } catch (EmailDejaUtiliseException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(409).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        return utilisateurService.modifierUtilisateur(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role-activation")
    public ResponseEntity<Utilisateur> modifierRoleEtActivation(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam boolean actif
    ) {
        Utilisateur utilisateur = utilisateurService.modifierRoleEtActivation(id, role, actif);
        return ResponseEntity.ok(utilisateur);
    }

    @PutMapping("/{id}/admin-update")
    public ResponseEntity<Utilisateur> adminUpdate(
            @PathVariable Long id,
            @RequestBody UtilisateurUpdateDTO dto
    ) {
        return utilisateurService.modifierRoleEtStatut(id, dto.getRole(), dto.getActif())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getUtilisateurConnecte(@AuthenticationPrincipal UserDetails userDetails) {
        return utilisateurService.getByEmail(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/non-actifs")
    public ResponseEntity<List<Utilisateur>> getUtilisateursNonActifs() {
        return ResponseEntity.ok(utilisateurService.getUtilisateursNonActifs());
    }
}