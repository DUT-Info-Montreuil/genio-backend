package com.genio.exception;
import com.genio.exception.business.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_ERROR = "error";

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

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFilterException(InvalidFilterException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(KEY_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailDejaUtiliseException.class)
    public ResponseEntity<?> handleEmailDejaUtilise(EmailDejaUtiliseException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }


}