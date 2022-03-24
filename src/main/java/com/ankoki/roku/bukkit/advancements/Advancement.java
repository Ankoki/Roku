package com.ankoki.roku.bukkit.advancements;

import com.ankoki.roku.bukkit.BukkitImpl;
import com.ankoki.roku.bukkit.advancements.exceptions.InvalidAdvancementException;
import com.ankoki.roku.web.JSONWrapper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For a better understanding of this class, see
 * <a href="https://minecraft.fandom.com/wiki/Advancement/JSON_format#File_format">this site</a>, which explains
 * where everything goes.
 */
public class Advancement {

    private final JSONWrapper json = new JSONWrapper();
    private NamespacedKey key;
    private org.bukkit.advancement.Advancement advancement;

    /**
     * Marks the given criteria as complete for the given advancement.
     * @param player the player to award.
     * @param advancement the advancement to progress.
     * @param criteria the criteria to complete.
     * @return true if awarded; false if criteria does not exist or already awarded.
     */
    public static boolean awardAdvancement(Player player, Advancement advancement, String criteria) {
        return player.getAdvancementProgress(advancement.getAdvancement()).awardCriteria(criteria);
    }

    /**
     * Marks the given criteria as complete for the given advancement.
     * @param player the player to award.
     * @param advancement the advancement to progress.
     * @param criteria the criteria to complete.
     * @return true if awarded; false if criteria does not exist or already awarded.
     */
    public static boolean awardAdvancement(Player player, org.bukkit.advancement.Advancement advancement, String criteria) {
        if (player == null || advancement == null || criteria == null) return false;
        return player.getAdvancementProgress(advancement).awardCriteria(criteria);
    }

    /**
     * Automatically checks off all criteria under an achievement, awarding it to the player.
     * @param player the player to give it to.
     * @param advancement the advancement to give.
     * @return true if awarded, false if not
     */
    public static boolean awardAdvancement(Player player, Advancement advancement) {
        if (player == null || advancement == null || advancement.getAdvancement() == null) return false;
        return Advancement.awardAdvancement(player, advancement.getAdvancement());
    }

    /**
     * Automatically checks off all criteria under an achievement, awarding it to the player.
     * @param player the player to give it to.
     * @param advancement the advancement to give.
     * @return true if awarded, false if not
     */
    public static boolean awardAdvancement(Player player, org.bukkit.advancement.Advancement advancement) {
        if (player == null || advancement == null) return false;
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        boolean awarded = true;
        for (String criteria : progress.getRemainingCriteria()) if (!progress.awardCriteria(criteria)) awarded = false;
        return awarded;
    }

    /**
     * Revokes a given criteria under an achievement.
     * @param player the player to revoke.
     * @param advancement the advancement.
     * @return true if the given criteria was revoked.
     */
    public static boolean revokeAdvancement(Player player, Advancement advancement, String criteria) {
        if (player == null || advancement == null || criteria == null) return false;
        return Advancement.revokeAdvancement(player, advancement.getAdvancement(), criteria);
    }

    /**
     * Revokes a given criteria under an achievement.
     * @param player the player to revoke.
     * @param advancement the advancement.
     * @return true if the given criteria was revoked.
     */
    public static boolean revokeAdvancement(Player player, org.bukkit.advancement.Advancement advancement, String criteria) {
        if (player == null || advancement == null || criteria == null) return false;
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        return progress.revokeCriteria(criteria);
    }

    /**
     * Automatically revokes all given criteria under an achievement, resetting it.
     * @param player the player to revoke.
     * @param advancement the advancement.
     * @return true if all criteria was revoked.
     */
    public static boolean revokeAdvancement(Player player, Advancement advancement) {
        if (player == null || advancement == null) return false;
        return Advancement.revokeAdvancement(player, advancement.getAdvancement());
    }

    /**
     * Automatically revokes all given criteria under an achievement, resetting it.
     * @param player the player to revoke.
     * @param advancement the advancement.
     * @return true if all criteria was revoked.
     */
    public static boolean revokeAdvancement(Player player, org.bukkit.advancement.Advancement advancement) {
        if (player == null || advancement == null) return false;
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        boolean revoked = true;
        for (String criteria : progress.getAwardedCriteria()) if (!progress.revokeCriteria(criteria)) revoked = false;
        return revoked;
    }

    /**
     * Removes an advancement from the persistent storage.
     * @param key the key of the advancement.
     * @param update true to reload data and remove from current server.
     *               <p> <strong>PLEASE NOTE</strong> <p>
     *               If you've updated loot tables or other advancements and not loaded the data for them,
     *               this shall also load them.
     * @return true if a file matching this key was found and deleted.
     */
    public static boolean removeAdvancement(NamespacedKey key, boolean update) {
        boolean b = BukkitImpl.getUnsafe().removeAdvancement(key);
        if (update && b) BukkitImpl.getInstance().getServer().reloadData();
        return b;
    }

    /**
     * Gets an advancement based on its key.
     * @param key key of advancement.
     * @return the found advancement, null if it doesn't exist.
     */
    public static org.bukkit.advancement.Advancement getAdvancement(NamespacedKey key) {
        return BukkitImpl.getInstance().getServer().getAdvancement(key);
    }

    /**
     * Checks if an advancement exists.
     * @param key the key to check.
     * @return true if it exists, false otherwise.
     */
    public static boolean advancementExists(NamespacedKey key) {
        return Advancement.getAdvancement(key) != null;
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Validates the JSON to make sure every required key is included,
     * as per https://minecraft.fandom.com/wiki/Advancement/JSON_format#File_format.
     * @param json json to validate.
     * @throws InvalidAdvancementException if there are any errors with the provided json.
     */
    private static void validateJson(JSONWrapper json) throws InvalidAdvancementException {
        if (json.containsKey("display")) {
            Object display = json.get("display");
            if (display instanceof Map<?,?> displayMap) {
                if (!displayMap.containsKey("title") || !displayMap.containsKey("description"))
                    throw new InvalidAdvancementException("Missing 'title' and/or 'description' value.");
                if (!displayMap.containsKey("icon")) throw new InvalidAdvancementException("Missing 'icon' value.");
                Object item = displayMap.get("icon");
                if (item instanceof Map itemMap) {
                    if (!itemMap.containsKey("item")) throw new InvalidAdvancementException();
                } else throw new InvalidAdvancementException();
            } else throw new InvalidAdvancementException();

        } else throw new InvalidAdvancementException("Missing 'display' value.");

        if (!json.containsKey("criteria")) throw new InvalidAdvancementException("Missing 'criteria' value.");
        Object criteria = json.get("criteria");
        if (criteria instanceof Map<?,?> criteriaMap) {
            for (Object object : criteriaMap.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                if (entry.getValue() instanceof Map criterionMap) {
                    if (!criterionMap.containsKey("trigger")) throw new InvalidAdvancementException("No trigger found in criterion '" + entry.getKey() + "'.");
                } else throw new InvalidAdvancementException();
            }
        } else throw new InvalidAdvancementException();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * See {@link Advancement#validateJson(JSONWrapper)}
     * @param advancement the advancement object containing the json to validate.
     * @throws InvalidAdvancementException
     */
    private static void validateJson(Advancement advancement) throws InvalidAdvancementException {
        Advancement.validateJson(advancement.getJson());
    }

    /**
     * Creates a new Advancement, and loads it from the given data.
     * @param key the key for the advancement.
     * @param json the JSON of the advancement, read more here https://minecraft.fandom.com/wiki/Advancement/JSON_format
     */
    public Advancement(NamespacedKey key, String json) {
        BukkitImpl.getUnsafe().loadAdvancement(key, json);
    }

    /**
     * Creates an empty advancement with a key.
     */
    public Advancement(NamespacedKey key) {
        this.key = key;
    }

    /**
     * Creates an empty advancement.
     */
    public Advancement() {}

    /**
     * Gets the json object that contains advancement information.
     * @return the json.
     */
    public JSONWrapper getJson() {
        return json;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * Sets the advancements NamespacedKey.
     * @param key the key.
     * @return current Advancement for chaining.
     */
    public Advancement setKey(NamespacedKey key) {
        this.key = key;
        return this;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * The item the icon will contain.
     * @param material the material.
     * @return current Advancement for chaining.
     */
    public Advancement setIcon(Material material) {
        // Can't use this.setMapValue() here because there is a parent and child map.
        Map<Object, Object> display = (Map<Object, Object>) json.getOrDefault("display", new HashMap<>());
        Map<Object, Object> icon = (Map<Object, Object>) display.getOrDefault("icon", new HashMap<>());
        icon.put("item", NamespacedKey.fromString(material.name().toLowerCase()));
        display.put("icon", icon);
        json.put("display", display);
        return this;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * The item the icon will contain.
     * @param stack the item.
     * @return current Advancement for chaining.
     */
    public Advancement setIcon(ItemStack stack) {
        this.setIcon(stack.getType());
        return this;
    }

    /**
     * A string containing <a href="https://minecraft.fandom.com/wiki/NBT_format#SNBT_format">SNBT</a>
     * that may modify the item's appearance on the icon.
     * @param snbt the SNBT.
     * @return current Advancement for chaining.
     */
    public Advancement setSNBT(String snbt) {
        // Can't use this.setMapValue() here because there is a parent and child map.
        Map<Object, Object> display = (Map<Object, Object>) json.getOrDefault("display", new HashMap<>());
        Map<Object, Object> icon = (Map<Object, Object>) display.getOrDefault("icon", new HashMap<>());
        icon.put("nbt", snbt);
        json.put("display", display);
        return this;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * A <a href="https://minecraft.fandom.com/wiki/Raw_JSON_text_format">JSON text component</a>
     * for the title of this advancement.
     * @param title the title.
     * @return current Advancement for chaining.
     */
    public Advancement setTitle(String title) {
        // TextComponent component = new TextComponent(title);
        // this.setMapValue("display", "title", ComponentSerializer.toString(component));
        this.setMapValue("display", "title", title);
        return this;
    }

    /**
     * Type of frame for this icon.
     * <p>
     * Defaults to {@link Frame#TASK}.
     * @param type the frame.
     * @return current Advancement for chaining.
     */
    public Advancement setFrame(Frame type) {
        this.setMapValue("display", "frame", type.toString());
        return this;
    }

    /**
     * The directory for the background to use in this advancement tab
     * (used only for the root advancement).
     * @param background the background.
     * @return current Advancement for chaining.
     */
    public Advancement setBackground(Background background) {
        this.setMapValue("display", "background", background.getLink());
        return this;
    }

    /**
     * The directory for the background to use in this advancement tab
     * (used only for the root advancement). Use namespaces, or {@link Advancement#setBackground(Background)}
     * @param path the path.
     * @return current Advancement for chaining.
     */
    public Advancement setBackground(String path) {
        this.setMapValue("display", "background", "path");
        return this;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * A <a href="https://minecraft.fandom.com/wiki/Raw_JSON_text_format">JSON text component</a>
     * for the description text of this advancement.
     * @param description the description.
     * @return current Advancement for chaining.
     */
    public Advancement setDescription(String description) {
        // TextComponent component = new TextComponent(description);
        // this.setMapValue("display", "description", ComponentSerializer.toString(component));
        this.setMapValue("display", "description", description);
        return this;
    }

    /**
     * Whether to announce in the chat when this advancement has been completed.
     * <p>
     * Defaults to true.
     * @param bool the boolean.
     * @return current Advancement for chaining.
     */
    public Advancement setAnnounced(boolean bool) {
        this.setMapValue("display", "announce_to_chat", bool);
        return this;
    }

    /**
     * Whether to hide this advancement and all its children from the advancement
     * screen until this advancement have been completed.
     * Has no effect on root advancements themselves, but still affects all their children.
     * <p>
     * Defaults to false.
     * @param bool whether to hide this advancement and all its children from the advancement until completed.
     * @return current Advancement for chaining.
     */
    public Advancement setHidden(boolean bool) {
        this.setMapValue("display", "hidden", bool);
        return this;
    }

    /**
     * The parent advancement directory of this advancement. If absent, this advancement is a root advancement.
     * Circular references cause a loading failure.
     * @param parent the parent directory.
     * @return current Advancement for chaining.
     */
    public Advancement setParent(String parent) {
        json.put("parent", parent);
        return this;
    }

    /**
     * <strong>REQUIRED</strong>
     * <p>
     * The criteria that have needed to grant the achievement.
     * @param criteria A unique name given to the criterion.
     * @param trigger The trigger for this advancement, specifies when this advancement
     *                should have its conditions checked and be completed.
     * @return current Advancement for chaining.
     * TODO add conditions.
     */
    public Advancement addCriteria(String criteria, AdvancementTrigger trigger) {
        // Can't use this.setMapValue() here because there is a parent and child map.
        Map<Object, Object> parent = (Map<Object, Object>) json.getOrDefault("criteria", new HashMap<>());
        Map<Object, Object> child = (Map<Object, Object>) parent.getOrDefault(criteria, new HashMap<>());
        child.put("trigger", trigger.toString());
        parent.put(criteria, child);
        json.put("criteria", parent);
        return this;
    }

    /**
     * Defines which criterions above must be completed to grant the advancement.
     * Contains sublists, which in turn contain names of criterions from this advancement.
     * <p>
     * Defaults to requiring all criterion completed.
     * @param map containing sublists of criterions.
     * @return current Advancement for chaining.
     */
    public Advancement setRequirements(Map map) {
        json.put("requirements", map);
        return this;
    }

    /**
     * An object representing the rewards provided when this advancement is obtained.
     * @param type <p>
     *             If type is {@link RewardType#RECIPE}, unlock recipes. Pass a list of namespaced IDs. <p>
     *             If type is {@link RewardType#LOOT}, give items from loot tables. Pass a list of namespaced IDs. <p>
     *             If type is {@link RewardType#EXPERIENCE}, give an amount of experience. Pass a number. <p>
     *             If type is {@link RewardType#FUNCTION}, run a function. Pass a namespace ID. Cannot run function tags.
     * @param reward the reward object to pass based on the type.
     * @return current Advancement for chaining.
     */
    public Advancement addReward(RewardType type, Object reward) {
        Map<Object, Object> parent = (Map<Object, Object>) json.getOrDefault("rewards", new HashMap<>());
        switch (type) {
            case RECIPE:
                List<String> rchild = (List<String>) parent.getOrDefault("recipes", new ArrayList<>());
                rchild.add((String) reward);
                parent.put("recipes", rchild);
                json.put("rewards", parent);
                break;
            case LOOT:
                List<String> lchild = (List<String>) parent.getOrDefault("loot", new ArrayList<>());
                lchild.add((String) reward);
                parent.put("loot", lchild);
                json.put("rewards", parent);
                break;
            case EXPERIENCE:
                this.setMapValue("rewards", "experience", reward);
                break;
            case FUNCTION:
                this.setMapValue("rewards", "function", reward);
                break;
        }
        return this;
    }

    /**
     * Returns the bukkit Advancement object, if loaded.
     * @return the advancement, or null.
     */
    @Nullable
    public org.bukkit.advancement.Advancement getAdvancement() {
        return advancement;
    }

    /**
     * Loads the advancement into the server.
     * @throws InvalidAdvancementException if there is issues with your JSON, or NamespacedKey isn't set.
     */
    public void load() throws InvalidAdvancementException {
        this.validateJson();
        if (key == null) throw new InvalidAdvancementException("The NamespacedKey was not set.");
        advancement = BukkitImpl.getUnsafe().loadAdvancement(key, json.toString());
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Gets the value in json of map, then the key in map and sets it to value, then sets the map in json to the updated.
     * @param map the map to get.
     * @param key the key to set. Must be a Map.
     * @param value the value to set in key.
     */
    private void setMapValue(String map, String key, Object value) {
        Map<Object, Object> m = (Map<Object, Object>) json.getOrDefault(map, new HashMap<>());
        m.put(key, value);
        json.put(map, m);
    }

    private void validateJson() throws InvalidAdvancementException {
        Advancement.validateJson(json);
    }
}
