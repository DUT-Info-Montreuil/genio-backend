package com.genio.exception.business;

public class NoConventionServicesAvailableException extends RuntimeException {
    public NoConventionServicesAvailableException(String message) {
        super(message);
    }
}