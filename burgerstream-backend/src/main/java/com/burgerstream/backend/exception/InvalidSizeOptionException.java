package com.burgerstream.backend.exception;

public class InvalidSizeOptionException extends RuntimeException {
    public InvalidSizeOptionException(String message) {
        super(message);
    }
}