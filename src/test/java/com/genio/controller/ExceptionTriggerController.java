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

package com.genio.controller;

import com.genio.exception.business.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTriggerController {

    @GetMapping("/test/invalid-format")
    public void throwInvalidFormat() {
        throw new InvalidFileFormatException("Format de fichier invalide");
    }

    @GetMapping("/test/empty-file")
    public void throwEmptyFile() {
        throw new EmptyFileException("Fichier vide");
    }

    @GetMapping("/test/file-too-large")
    public void throwFileTooLarge() {
        throw new FileTooLargeException("Fichier trop volumineux");
    }

    @GetMapping("/test/empty-dir")
    public void throwEmptyDirectory() {
        throw new EmptyDirectoryException("Répertoire vide");
    }

    @GetMapping("/test/already-exists")
    public void throwAlreadyExists() {
        throw new ConventionServiceAlreadyExistsException("existe déjà");
    }

    @GetMapping("/test/in-use")
    public void throwInUse() {
        throw new ConventionServiceInUseException("utilisé");
    }

    @GetMapping("/test/invalid-filter")
    public void throwInvalidFilter() throws InvalidFilterException {
        throw new InvalidFilterException("Filtre invalide");
    }

    @GetMapping("/test/unexpected")
    public void throwUnexpected() {
        throw new RuntimeException("Oups !");
    }
}