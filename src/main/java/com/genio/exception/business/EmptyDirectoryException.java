package com.genio.exception.business;

public class EmptyDirectoryException extends RuntimeException {
    public EmptyDirectoryException(String message) {
        super(message);
    }
}