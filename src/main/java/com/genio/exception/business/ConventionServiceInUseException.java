package com.genio.exception.business;

public class ConventionServiceInUseException extends RuntimeException {
    public ConventionServiceInUseException(String message) {
        super(message);
    }
}