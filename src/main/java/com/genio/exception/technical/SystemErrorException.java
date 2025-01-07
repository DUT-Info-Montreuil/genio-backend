package com.genio.exception.technical;

public class SystemErrorException extends RuntimeException {
    public SystemErrorException(String message) {
        super(message);
    }
}