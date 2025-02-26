package com.genio.exception.business;

public class NoTemplatesAvailableException extends RuntimeException {
    public NoTemplatesAvailableException(String message) {
        super(message);
    }
}