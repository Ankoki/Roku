package com.ankoki.roku.web.json.exceptions;

public class MalformedJsonException extends RuntimeException {

    public MalformedJsonException() {
        super("There was an issue parsing your JSON.");
    }

    public MalformedJsonException(String message) {
        super(message);
    }

}
