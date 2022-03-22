package com.ankoki.roku.bukkit.advancements;

public enum Background {
    ADVENTURE("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
    END("minecraft:textures/gui/advancements/backgrounds/end.png"),
    HUSBANDRY("minecraft:textures/gui/advancements/backgrounds/husbandry.png"),
    NETHER("minecraft:textures/gui/advancements/backgrounds/nether.png"),
    STONE("minecraft:textures/gui/advancements/backgrounds/stone.png");

    private final String link;
    Background(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
