package com.genio.exception.business;

/**
 * Exception levée lorsqu'une erreur survient lors de la génération d'une convention.
 * Par exemple, si une étape critique échoue pendant le traitement.
 */
public class GenerationConventionException extends RuntimeException {
    public GenerationConventionException(String message) {
        super(message);
    }
}
