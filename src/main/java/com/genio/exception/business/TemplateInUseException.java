package com.genio.exception.business;

public class TemplateInUseException extends RuntimeException {
    public TemplateInUseException(String message) {
        super(message);
    }
}