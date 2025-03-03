package com.genio.exception.business;

public class IntegrityCheckFailedException extends Exception {
    public IntegrityCheckFailedException(String message) {
        super(message);
    }
}