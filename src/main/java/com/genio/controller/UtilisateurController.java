package com.genio.controller;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;
import com.genio.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

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
}