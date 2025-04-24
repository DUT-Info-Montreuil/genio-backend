package com.genio.controller;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;

import com.genio.service.impl.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}