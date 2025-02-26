package com.genio.exception.business;

public class NoErrorFoundException extends RuntimeException {
    public NoErrorFoundException(String message) {
        super(message);
    }
}