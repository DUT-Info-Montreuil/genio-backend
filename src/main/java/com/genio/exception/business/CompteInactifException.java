package com.genio.exception.business;

public class CompteInactifException extends RuntimeException {
    public CompteInactifException(String message) {
        super(message);
    }
}