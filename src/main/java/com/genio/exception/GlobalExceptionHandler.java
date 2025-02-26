package com.genio.exception;

import com.genio.exception.business.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue : " + ex.getMessage());
    }

    @ExceptionHandler(TemplateAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTemplateAlreadyExistsException(TemplateAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Un modèle avec ce nom existe déjà");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TemplateInUseException.class)
    public ResponseEntity<Map<String, String>> handleTemplateInUseException(TemplateInUseException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Modèle en cours d'utilisation");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFilterException(InvalidFilterException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}