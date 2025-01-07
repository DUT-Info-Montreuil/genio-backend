package com.genio.exception.business;

/**
 * Exception levée lorsque les données fournies par l'utilisateur sont invalides ou incomplètes.
 * Utilisée pour signaler une violation des règles métier ou des données manquantes.
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
