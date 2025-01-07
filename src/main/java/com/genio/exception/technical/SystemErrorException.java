package com.genio.exception.technical;

/**
 * Exception levée lorsqu'une erreur système inattendue survient.
 * Par exemple, un problème de connexion au système de fichiers ou à une base de données.
 */
public class SystemErrorException extends RuntimeException {
    public SystemErrorException(String message) {
        super(message);
    }
}