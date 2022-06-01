package com.example.demo.exception;

public class NotFoundCrudException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not Found";

    public NotFoundCrudException(String message) {
        super(message);
    }

    public NotFoundCrudException() {
        super(DEFAULT_MESSAGE);
    }
}