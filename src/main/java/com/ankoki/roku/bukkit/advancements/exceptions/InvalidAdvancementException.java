package com.ankoki.roku.bukkit.advancements.exceptions;

public class InvalidAdvancementException extends Exception {

    private final String message;

    public InvalidAdvancementException() {
        this.message = "There was an issue with your advancement!";
    }

    public InvalidAdvancementException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
