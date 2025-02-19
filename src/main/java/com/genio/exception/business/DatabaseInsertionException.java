package com.genio.exception.business;

public class DatabaseInsertionException extends RuntimeException {
    public DatabaseInsertionException(String message, Throwable cause) {
        super(message, cause);
    }
}