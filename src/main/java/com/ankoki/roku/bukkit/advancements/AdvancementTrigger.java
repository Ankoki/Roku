package com.ankoki.roku.bukkit.advancements;

// https://minecraft.fandom.com/wiki/Advancement/JSON_format
public enum AdvancementTrigger {
    BEE_NEST_DESTROYED("block", "item", "num_bees_inside", "player"),
    BRED_ANIMALS("child", "parent", "partner", "player"),
    BREWED_POTION("potion", "player"),
    CHANGED_DIMENSION("from", "to", "player"),
    CHANNELED_LIGHTNING("victims", "player"),
    CONSTRUCT_BEACON("level", "player"),
    CONSUME_ITEM("item", "player"),
    CURED_ZOMBIE_VILLAGER("villager", "zombie", "player"),
    EFFECTS_CHANGED("effects", "source", "player"),
    ENCHANTED_ITEM("item", "levels", "player"),
    ENTER_BLOCK("block", "state", "player"),
    ENTITY_HURT_PLAYER("damage", "player"),
    ENTITY_KILLED_PLAYER("entity", "killing_blow", "player"),
    FALL_FROM_HEIGHT("player", "start_position", "distance"),
    FILLED_BUCKET("item", "player"),
    FISHING_ROD_HOOKED("entity", "item", "rod", "player"),
    HERO_OF_THE_VILLAGE("location", "player"),
    IMPOSSIBLE(),
    INVENTORY_CHANGED("items", "slots", "player"),
    ITEM_DURABILITY_CHANGED("delta", "durability", "item", "player"),
    ITEM_USED_ON_BLOCK("location", "item", "player"),
    KILLED_BY_CROSSBOW("unique_entity_types", "victims", "player"),
    LEVITATION("distance", "duration", "player"),
    LIGHTNING_STRIKE("lightning", "bystander", "player"),
    LOCATION("location", "player"),
    NETHER_TRAVEL("entered", "exited", "distance", "player"),
    PLACED_BLOCK("block", "item", "location", "state", "player"),
    PLAYER_GENERATES_CONTAINER_LOOT("loot_table", "player"),
    PLAYER_HURT_ENTITY("damage", "entity", "player"),
    PLAYER_INTERACTED_WITH_ENTITY("item", "entity", "player"),
    PLAYER_KILLED_ENTITY("entity", "killing_blow", "player"),
    RECIPE_UNLOCKED("recipe", "player"),
    SHOT_CROSSBOW("item", "player"),
    SLEPT_IN_BED("location", "player"),
    SLIDE_DOWN_BLOCK("block", "player"),
    STARTED_RIDING("player"),
    SUMMONED_ENTITY("entity", "player"),
    TAME_ANIMAL("entity", "player"),
    TARGET_HIT("signal_strength", "projectile", "shooter", "player"),
    THROWN_ITEM_PICKED_UP_BY_ENTITY("item", "entity", "player"),
    TICK("player"),
    USED_ENDER_EYE("distance", "player"),
    USED_TOTEM("item", "player"),
    USING_ITEM("item", "player"),
    VILLAGER_TRADE("item", "villager", "player"),
    VOLUNTARY_EXILE("location", "player");

    private final String[] conditions;
    AdvancementTrigger(String... conditions) {
        this.conditions = conditions;
    }
    public String[] getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
