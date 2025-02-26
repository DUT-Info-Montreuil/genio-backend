package com.genio.controller;

import com.genio.dto.output.ErreurDTO;
import com.genio.exception.business.InvalidFilterException;
import com.genio.exception.business.NoErrorFoundException;
import com.genio.service.impl.ErreurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/erreurs")
public class ErreurController {

    @Autowired
    private ErreurService erreurService;

    @GetMapping
    public ResponseEntity<List<ErreurDTO>> getAllErrors() {
        try {
            List<ErreurDTO> erreurs = erreurService.getRecentErrors();

            if (erreurs.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(erreurs);
        } catch (NoErrorFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of(new ErreurDTO("Impossible de récupérer les erreurs")));
        }
    }
}