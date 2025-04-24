package com.genio.service;

import com.genio.dto.UtilisateurDTO;
import com.genio.model.Utilisateur;
import com.genio.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Utilisateur creerUtilisateur(UtilisateurDTO dto) {
        Utilisateur utilisateur = Utilisateur.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .username(dto.getUsername())
                .motDePasse(passwordEncoder.encode(dto.getMotDePasse()))
                .role("UTILISATEUR")
                .actif(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    public Optional<Utilisateur> modifierUtilisateur(Long id, UtilisateurDTO dto) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateur.setNom(dto.getNom());
            utilisateur.setPrenom(dto.getPrenom());
            utilisateur.setUsername(dto.getUsername());
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
            utilisateur.setUpdatedAt(LocalDateTime.now());
            return utilisateurRepository.save(utilisateur);
        });
    }
}