package com.ankoki.roku.bukkit.guis;

import com.ankoki.roku.misc.Pair;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// finish then make public.
class PaginatedGUI extends GUI {

    private final Map<Pair<String, Integer>, GUI> GUI_PAGES = new ConcurrentHashMap<>();

    /**
     * Creates a new GUI inventory.
     * @param holder the owner.
     * @param name the title.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(InventoryHolder holder, String name, int size) {
        super(holder, name, size);
    }

    /**
     * Creates a new GUI inventory.
     * @param name the title.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(String name, int size) {
        super(null, name, size);
    }

    /**
     * Creates a new GUI inventory.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public PaginatedGUI(int size) {
        super(null, null, size);
    }

    /**
     * Creates a new GUI inventory.
     * @param holder the owner.
     * @param name the title.
     * @param type the type.
     */
    public PaginatedGUI(InventoryHolder holder, String name, InventoryType type) {
        super(holder, name, type);
    }

    /**
     * Creates a new GUI inventory.
     * @param name the title.
     * @param type the type.
     */
    public PaginatedGUI(String name, InventoryType type) {
        super(null, name, type);
    }

    /**
     * Creates a new GUI inventory.
     * @param type the type.
     */
    public PaginatedGUI(InventoryType type) {
        super(null, null, type);
    }

    public PaginatedGUI registerPage(String key, int number) {

        return this;
    }
}
