package com.genio.exception.business;

public class DocxGenerationException extends RuntimeException {
    public DocxGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}