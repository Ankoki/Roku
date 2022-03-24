package com.ankoki.roku.bukkit.guis;

import com.ankoki.roku.bukkit.guis.events.ClickEvent;
import com.ankoki.roku.bukkit.guis.events.DragEvent;
import com.ankoki.roku.misc.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GUI extends IGUI {

    private static final List<IGUI> GUI_REGISTRY = new ArrayList<>();

    /**
     * Opens the GUI to the given entities.
     * @param gui gui to open.
     * @param entities entities to show.
     * @return if opened successfully.
     */
    public static boolean openGui(IGUI gui, HumanEntity... entities) {
        Inventory inventory = gui.getInventory();
        if (inventory == null) return false;
        for (HumanEntity entity : entities) entity.openInventory(inventory);
        return true;
    }

    /**
     * Registers an IGUI, enabling the following methods:
     * <p> - {@link IGUI#onClick(InventoryClickEvent)}
     * <p> - {@link IGUI#onDrag(InventoryDragEvent)}
     * @param gui the IGUI to register.
     * @return if registered successfully.
     */
    public static boolean registerGUI(IGUI gui) {
        if (GUI_REGISTRY.contains(gui)) return false;
        return GUI_REGISTRY.add(gui);
    }

    /**
     * Gets the bukkit Inventory of an IGUI.
     * @param gui the IGUI.
     * @return the Inventory.
     */
    public static Inventory getInventory(IGUI gui) {
        return gui.getInventory();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Gets the current registry.
     * @return the registry.
     */
    protected static List<IGUI> getRegistry() {
        return GUI.GUI_REGISTRY;
    }

    private String name;
    private InventoryHolder owner;
    private InventoryType type;
    private int size;
    private boolean canClickOwn = false;

    private final Map<Integer, ClickEvent> clickEvents = new ConcurrentHashMap<>();
    private DragEvent dragEvent = null;

    @Override
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Sets the current GUIs name.
     * @param name new name.
     * @return current GUI for chaining.
     */
    public GUI setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    @Nullable
    public InventoryHolder getOwner() {
        return owner;
    }

    /**
     * Sets the current GUIs owner.
     * @param owner new owner.
     * @return current GUI for chaining.
     */
    public GUI setOwner(InventoryHolder owner) {
        this.owner = owner;
        return this;
    }

    @Override
    @Nullable
    public InventoryType getType() {
        return type;
    }

    /**
     * Sets the current GUIs type.
     * @param type new type.
     * @return current GUI for chaining.
     */
    public GUI setType(InventoryType type) {
        this.type = type;
        return this;
    }

    @Override
    public int getSize() {
        return size;
    }

    /**
     * Sets the current GUIs size.
     * @param size new size.
     * @return current GUI for chaining.
     */
    public GUI setSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * Checks if entity can click or drag their own inventory.
     * <p>
     * If true, they can move stuff around in their inventory, else
     * it will get cancelled. Defaults to false.
     * @return if entity can click or drag their own inventory.
     */
    public boolean canClickOwnInventory() {
        return this.canClickOwn;
    }

    /**
     * Sets if entity can click or drag their own inventory.
     * @param canClickOwn if entity can click or drag in their inventory.
     * @return current GUI for chaining.
     */
    public GUI canClickOwnInventory(boolean canClickOwn) {
        this.canClickOwn = canClickOwn;
        return this;
    }

    /**
     * Adds to the current GUIs click event.
     * @param event the runnable you want to be run when clicked.
     * @param slots the slots this should run for. All slots if none specified.
     * @return current GUI for chaining.
     */
    public GUI addClickEvent(ClickEvent event, int... slots) {
        if (slots.length == 0) slots = Utils.range(0, 54);
        for (int i : slots) clickEvents.put(i, event);
        return this;
    }

    /**
     * Sets the current GUIs drag event.
     * @param event the runnable you want to be run when clicked.
     * @return current GUI for chaining.
     */
    public GUI setDragEvent(DragEvent event) {
        this.dragEvent = event;
        return this;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (event.getClickedInventory() == entity.getInventory()) event.setCancelled(!this.canClickOwnInventory());
        else {
            int slot = event.getSlot();
            if (clickEvents.containsKey(slot)) {
                ClickEvent e = clickEvents.get(slot);
                if (e == null) return;
                e.onClick(event);
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (event.getInventory() == entity.getInventory()) event.setCancelled(!this.canClickOwnInventory());
        else if (dragEvent != null) dragEvent.onDrag(event);
    }
}
