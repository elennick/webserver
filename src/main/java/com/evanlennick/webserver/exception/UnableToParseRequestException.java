package com.evanlennick.webserver.exception;

public class UnableToParseRequestException extends RuntimeException {
    public UnableToParseRequestException(Throwable cause) {
        super(cause);
    }
}
