package com.genio.exception.business;

public class UnreplacedPlaceholderException extends RuntimeException {
    public UnreplacedPlaceholderException(String message) {
        super(message);
    }
}