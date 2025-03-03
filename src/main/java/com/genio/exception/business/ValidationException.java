package com.genio.exception.business;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}