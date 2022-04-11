package com.ankoki.roku.bukkit.guis;

import com.ankoki.roku.bukkit.BukkitImpl;
import com.ankoki.roku.misc.Pair;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaginatedGUI extends GUI {

    private static final NamespacedKey BUTTON_KEY = new NamespacedKey(BukkitImpl.getInstance(), "ROKU_BUTTON");
    private final Map<String, Pair<Integer, GUI>> currentPages = new ConcurrentHashMap<>();

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param holder the owner.
     * @param name   the title.
     * @param size   the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(InventoryHolder holder, String name, int size) {
        super(holder, name, size);
        currentPages.put("DEFAULT", new Pair<>(0, new GUI(holder, name, size)));
    }

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param name the title.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(String name, int size) {
        super(null, name, size);
    }

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(int size) {
        super(null, null, size);
    }

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param holder the owner.
     * @param name   the title.
     * @param type   the type.
     */
    public PaginatedGUI(InventoryHolder holder, String name, InventoryType type) {
        super(holder, name, type);
    }

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param name the title.
     * @param type the type.
     */
    public PaginatedGUI(String name, InventoryType type) {
        super(null, name, type);
    }

    /**
     * Creates a new PaginatedGUI inventory.
     *
     * @param type the type.
     */
    public PaginatedGUI(InventoryType type) {
        super(null, null, type);
    }

    /**
     * Creates a new PaginatedGUI with the given GUI as the first page.
     *
     * @param gui the gui.
     */
    public PaginatedGUI(GUI gui) {
        super(gui);
    }

    /**
     * Registers a new page under the given key and index.
     *
     * @param key   the key. Cannot be 'DEFAULT'.
     * @param index the index. Cannot be 0.
     * @param gui   the GUI of this page. Can be empty.
     * @return current PaginatedGUI for chaining.
     */
    public PaginatedGUI registerPage(String key, int index, GUI gui) {
        if (key.equalsIgnoreCase("DEFAULT"))
            throw new IllegalArgumentException("You cannot register a GUI with 'DEFAULT', as the main inventory is under that key.");
        if (index <= 0)
            throw new IllegalArgumentException("You cannot register a GUI with the index 0, as the main inventory has that index.");
        if (this.currentPages.containsKey(key))
            throw new IllegalArgumentException("There is already a page registered under '" + key + "'");
        for (Map.Entry<String, Pair<Integer, GUI>> entry : this.currentPages.entrySet()) {
            Pair<Integer, GUI> pair = entry.getValue();
            if (pair.getFirst() == index)
                throw new IllegalArgumentException("A page is already registered under index " + index + ". Registered under '" + entry.getKey() + "'");
        }
        this.currentPages.put(key, new Pair<>(index, gui));
        GUI.registerGUI(gui);
        return this;
    }

    /**
     * Sets the slot of a page.
     *
     * @param key  the key of the page.
     * @param item the item.
     * @param slot the slot.
     * @return current PaginatedGUI for chaining.
     */
    public PaginatedGUI setSlot(@NotNull String key, @NotNull ItemStack item, int slot) {
        if (!this.currentPages.containsKey(key))
            throw new IllegalArgumentException("There is no page registered under '" + key + "'");
        Pair<Integer, GUI> pair = this.currentPages.get(key);
        GUI gui = pair.getSecond();
        gui.setSlot(slot, item);
        pair.setSecond(gui);
        this.currentPages.put(key, pair);
        return this;
    }

    /**
     * Sets the slot of a page.
     *
     * @param index the index of the page you want to change.
     * @param item  the item.
     * @param slot  the slot.
     * @return current PaginatedGUI for chaining.
     */
    public PaginatedGUI setSlot(int index, @NotNull ItemStack item, int slot) {
        String key = this.fromIndex(index);
        if (key == null) throw new IllegalArgumentException("There is no page registered under index " + index);
        return this.setSlot(key, item, slot);
    }

    /**
     * Gets the ItemStack which will act as a button to a page.
     * @param page the page.
     * @param item the base item.
     * @return the item which will act as a button.
     */
    public static ItemStack makeButton(@NotNull String page, @NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer()
                .set(BUTTON_KEY, PersistentDataType.STRING, page);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Opens the page at the given index.
     * @param index the index.
     * @param entities the entities to open.
     * @return current PaginatedGUI for chaining.
     */
    public PaginatedGUI openPage(int index, HumanEntity... entities) {
        String page = this.fromIndex(index);
        if (page == null) throw new IllegalArgumentException("There is no page under index " + index);
        return this.openPage(page, entities);
    }

    /**
     * Opens the page under the given name.
     * @param page the page.
     * @param entities the entities to open.
     * @return current PaginatedGUI for chaining.
     */
    public PaginatedGUI openPage(String page, HumanEntity... entities) {
        if (!this.currentPages.containsKey(page)) throw new IllegalArgumentException("There is no page registered under '" + page + "'");
        Pair<Integer, GUI> pair = this.currentPages.get(page);
        GUI.openGui(pair.getSecond(), entities);
        return this;
    }

    /**
     * Opens the page from a button.
     * @param item the button.
     * @param entities the entities to open.
     * @return if the item was a button.
     */
    public boolean openPage(@NotNull ItemStack item, HumanEntity... entities) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getPersistentDataContainer().get(BUTTON_KEY, PersistentDataType.STRING) != null) {
            String page = meta.getPersistentDataContainer().get(BUTTON_KEY, PersistentDataType.STRING);
            this.openPage(page, entities);
            return true;
        } return false;
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Gets the key of a page from the index.
     *
     * @param index the index to look for.
     * @return the index to search for.
     */
    private String fromIndex(int index) {
        for (Map.Entry<String, Pair<Integer, GUI>> entry : this.currentPages.entrySet()) {
            Pair<Integer, GUI> pair = entry.getValue();
            if (pair.getFirst() == index) return entry.getKey();
        }
        return null;
    }
}
