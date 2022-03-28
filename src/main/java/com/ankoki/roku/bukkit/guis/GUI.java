package com.ankoki.roku.bukkit.guis;

import com.ankoki.roku.bukkit.guis.events.ClickEvent;
import com.ankoki.roku.bukkit.guis.events.DragEvent;
import com.ankoki.roku.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GUI extends IGUI {

    private static final List<IGUI> GUI_REGISTRY = new ArrayList<>();

    /**
     * Opens the GUI to the given entities.
     *
     * @param gui      gui to open.
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
     *
     * @param gui the IGUI to register.
     * @return if registered successfully.
     */
    public static boolean registerGUI(IGUI gui) {
        if (GUI_REGISTRY.contains(gui)) return false;
        return GUI_REGISTRY.add(gui);
    }

    /**
     * Gets the bukkit Inventory of an IGUI.
     *
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
     *
     * @return the registry.
     */
    protected static List<IGUI> getRegistry() {
        return GUI.GUI_REGISTRY;
    }

    private final Inventory inventory;
    private List<String> shape;
    private boolean canClickOwn = false;

    private final Map<Integer, ClickEvent> clickEvents = new ConcurrentHashMap<>();
    private DragEvent dragEvent = null;

    /**
     * Creates a new GUI inventory.
     * @param holder the owner.
     * @param name the title.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public GUI(InventoryHolder holder, String name, int size) {
        this.validateSize(size);
        name = name == null ? "§f" : name;
        inventory = Bukkit.createInventory(holder, size, name);
    }

    /**
     * Creates a new GUI inventory.
     * @param name the title.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public GUI(String name, int size) {
        this(null, name, size);
    }

    /**
     * Creates a new GUI inventory.
     * @param size the size. Must be a multiple of 9, between 9 and 54.
     */
    public GUI(int size) {
        this(null, null, size);
    }

    /**
     * Creates a new GUI inventory.
     * @param holder the owner.
     * @param name the title.
     * @param type the type.
     */
    public GUI(InventoryHolder holder, String name, InventoryType type) {
        name = name == null ? "§f" : name;
        inventory = Bukkit.createInventory(holder, type, name);
    }

    /**
     * Creates a new GUI inventory.
     * @param name the title.
     * @param type the type.
     */
    public GUI(String name, InventoryType type) {
        this(null, name, type);
    }

    /**
     * Creates a new GUI inventory.
     * @param type the type.
     */
    public GUI(InventoryType type) {
        this(null, null, type);
    }

    /**
     * Sets the shape of a gui.
     * @param shape the shape. This list should be the amount of rows this GUI has. <p>
     *              Each string should be 9 characters long. <p>
     *              Each character represents an item; imagine I had a gui of 3 rows. <p>
     *              I call <code>myGui.setShape(List.of("xxxxxxxxx", "xxxxAxxxx", "xxxxxxxxx");</code> <p>
     *              The shape is now set, now we can use the {@link GUI#setShapeItem(char, ItemStack)} method. <p>
     *              Firstly, I want to set all occurrences of 'x' to black stained-glass panes. <p>
     *              To do this, I will call <code>myGui.setShapeItem('x', new ItemStack(Material.BLACK_STAINED_GLASS_PANE);</code> <p>
     *              Now the GUI is black stained-glass panes, however the middle slot where the A is is still empty. <p>
     *              I want that to be red wool; and we can use the same method, just with different parameters. <p>
     *              <code>myGui.setShapeItem('A', new ItemStack(Material.RED_WOOL);</code> would do the job.
     * @return current GUI for chaining.
     */
    public GUI setShape(@NotNull List<String> shape) {
        int rows = inventory.getSize() / 9;
        if (rows != shape.size())
            throw new IllegalArgumentException("The shape needs to have the same amount of strings as rows.");
        for (String row : shape)
            if (row.length() != 9) throw new IllegalArgumentException("Each row must only have 9 characters in, representing each slot.");
        this.shape = shape;
        return this;
    }

    /**
     * Sets the character to the given item in the shape. Must call {@link GUI#setShape(List)} first.
     * @param character the character to set. See {@link GUI#setShape(List)} for more information.
     * @param stack the item to set it to.
     * @return current GUI for chaining.
     */
    public GUI setShapeItem(@NotNull char character, ItemStack stack) {
        if (this.shape == null) throw new IllegalStateException("You need to set the shape before setting the items.");
        int slot = 0;
        for (String row : shape) {
            for (char c : row.toCharArray()) {
                if (character == c) inventory.setItem(slot, stack);
                slot++;
            }
        }
        return this;
    }

    /**
     * Checks if entity can click or drag their own inventory.
     * <p>
     * If true, they can move stuff around in their inventory, else
     * it will get cancelled. Defaults to false.
     *
     * @return if entity can click or drag their own inventory.
     */
    public boolean canClickOwnInventory() {
        return this.canClickOwn;
    }

    /**
     * Sets if entity can click or drag their own inventory.
     *
     * @param canClickOwn if entity can click or drag in their inventory.
     * @return current GUI for chaining.
     */
    public GUI canClickOwnInventory(boolean canClickOwn) {
        this.canClickOwn = canClickOwn;
        return this;
    }

    /**
     * Adds to the current GUIs click event.
     *
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
     *
     * @param event the runnable you want to be run when clicked.
     * @return current GUI for chaining.
     */
    public GUI setDragEvent(DragEvent event) {
        this.dragEvent = event;
        return this;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets a slot in the GUI.
     * @param slot the slot to change.
     * @param item the item to change it to.
     * @return current GUI for changing.
     */
    public GUI setSlot(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    /**
     * Adds items to the next available space in the GUI.
     * @param items the items to add.
     * @return current GUI for chaining.
     */
    public GUI addItem(ItemStack... items) {
        inventory.addItem(items);
        return this;
    }

    /**
     * Registers the current GUI, enabling the following methods:
     * <p> - {@link IGUI#onClick(InventoryClickEvent)}
     * <p> - {@link IGUI#onDrag(InventoryDragEvent)}
     * @return if registered successfully.
     */
    public boolean registerGUI() {
        return GUI.registerGUI(this);
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
