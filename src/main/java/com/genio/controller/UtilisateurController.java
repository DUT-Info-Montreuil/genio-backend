package com.genio.controller;

import com.genio.dto.UtilisateurDTO;
import com.genio.dto.UtilisateurUpdateDTO;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import com.genio.service.impl.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping
    public ResponseEntity<Utilisateur> creer(@RequestBody UtilisateurDTO dto) {
        Utilisateur u = utilisateurService.creerUtilisateur(dto);
        return ResponseEntity.ok(u);
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
    public Utilisateur getUtilisateurConnecte(@AuthenticationPrincipal UserDetails userDetails) {
        return utilisateurRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}