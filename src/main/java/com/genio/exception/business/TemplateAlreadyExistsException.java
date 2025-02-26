package com.genio.exception.business;

public class TemplateAlreadyExistsException extends RuntimeException {
    public TemplateAlreadyExistsException(String message) {
        super(message);
    }
}