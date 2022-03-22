package com.ankoki.roku.bukkit.advancements;

public enum RewardType {
    RECIPE, LOOT, EXPERIENCE, FUNCTION;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
