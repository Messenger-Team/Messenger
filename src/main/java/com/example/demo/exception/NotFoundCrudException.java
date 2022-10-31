package com.example.demo.exception;

import java.util.Date;

public class NotFoundCrudException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not Found";
    private static final String CONNECTED_FAILED_MESSAGE = "Chrl idi perezagruxzi server, time = ";

    public NotFoundCrudException(String message) {
        super(message);
    }

    public NotFoundCrudException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundCrudException(Date time){ super(CONNECTED_FAILED_MESSAGE + time.toString());}
}