package com.ankoki.roku.web.exceptions;

public class MalformedJsonException extends Exception {

    private final String message;

    public MalformedJsonException() {
        this.message = "There was an issue parsing your JSON!";
    }

    public MalformedJsonException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
