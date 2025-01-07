package com.genio.exception.business;

/**
 * Exception levée lorsque le modèle demandé est introuvable dans la base de données.
 */
public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String message) {
        super(message);
    }
}
