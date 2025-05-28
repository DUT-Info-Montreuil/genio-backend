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

package com.genio.exception;
import com.genio.utils.ErreurType;
import com.genio.config.ErreurDetaillee;
import com.genio.exception.business.*;
import com.genio.service.impl.HistorisationService;
import com.genio.utils.ErrorMessageTranslator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_ERROR = "error";
    private static final String STATUS_ECHEC = "ECHEC";


    private final HistorisationService historisationService;

    public GlobalExceptionHandler(HistorisationService historisationService) {
        this.historisationService = historisationService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        historisationService.sauvegarderHistorisation(
                null,
                null,
                null,
                STATUS_ECHEC,
                List.of(new ErreurDetaillee("exception", ex.getMessage(), ErreurType.TECHNIQUE))
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur interne est survenue : " + ex.getMessage());
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFilterException(InvalidFilterException ex) {
        List<ErreurDetaillee> erreurs = List.of(new ErreurDetaillee("filtre", ex.getMessage(), ErreurType.FLUX));
        historisationService.sauvegarderHistorisation(null, null, null,  STATUS_ECHEC, erreurs);

        Map<String, String> response = new HashMap<>();
        response.put(KEY_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<String> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<String> handleEmptyFileException(EmptyFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<String> handleFileTooLargeException(FileTooLargeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmptyDirectoryException.class)
    public ResponseEntity<String> handleEmptyDirectoryException(EmptyDirectoryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(ConventionServiceAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleConventionServiceAlreadyExistsException(ConventionServiceAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(KEY_ERROR, "Un modèle avec ce nom existe déjà");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConventionServiceInUseException.class)
    public ResponseEntity<Map<String, String>> handleConventionServiceInUseException(ConventionServiceInUseException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(KEY_ERROR, "Modèle en cours d'utilisation");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmailDejaUtiliseException.class)
    public ResponseEntity<Map<String, String>> handleEmailDejaUtilise(EmailDejaUtiliseException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(KEY_ERROR, ex.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String champ = "corps";
        String message = "Le corps de la requête est manquant ou mal formé.";

        List<ErreurDetaillee> erreurs = List.of(
                new ErreurDetaillee(champ, message, ErreurType.JSON)
        );

        historisationService.sauvegarderHistorisation(null, null, null,  STATUS_ECHEC, erreurs);

        Map<String, String> response = new HashMap<>();
        response.put(champ, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> messagesLisibles = new HashMap<>();
        List<ErreurDetaillee> erreurs = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String champ = error.getField();
            String messageTraduit = ErrorMessageTranslator.translate(champ, error.getDefaultMessage());

            messagesLisibles.put(champ, messageTraduit);
            erreurs.add(new ErreurDetaillee(champ, messageTraduit, ErreurType.JSON));
        }

        historisationService.sauvegarderHistorisation(null, null, null,  STATUS_ECHEC, erreurs);

        return new ResponseEntity<>(messagesLisibles, HttpStatus.BAD_REQUEST);
    }


}