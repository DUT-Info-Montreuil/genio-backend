package com.genio.exception.business;

public class ConventionServiceAlreadyExistsException extends RuntimeException {
    public ConventionServiceAlreadyExistsException(String message) {
        super(message);
    }
}