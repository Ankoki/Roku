package com.ankoki.roku.bukkit.advancements;

public enum Frame {
    CHALLENGE, GOAL, TASK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
