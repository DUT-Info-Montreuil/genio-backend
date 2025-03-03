package com.genio.exception.business;

public class UnauthorizedModificationException extends Exception {
    public UnauthorizedModificationException(String message) {
        super(message);
    }
}