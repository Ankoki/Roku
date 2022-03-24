package com.ankoki.roku.bukkit.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

public abstract class IGUI {

    /**
     * Gets the name of the GUI. Can be null.
     * @return the name of the GUI.
     */
    public abstract @Nullable String getName();

    /**
     * Gets who owns the GUI. Can be null.
     * @return the GUI owner.
     */
    public abstract @Nullable InventoryHolder getOwner();

    /**
     * Gets the type of inventory. Can be null.
     * @return the inventory type.
     */
    public abstract @Nullable InventoryType getType();

    /**
     * The size of an inventory.
     * <p>
     * Cannot be over 54 and must be a multiple of 9.
     * @return the GUI size.
     */
    public abstract int getSize();

    public void onClick(InventoryClickEvent event){}
    public void onDrag(InventoryDragEvent event){}

    /**
     * Opens the GUI to entites.
     * @param entities the entities to open the inventory to.
     */
    public void openTo(HumanEntity... entities) {
        GUI.openGui(this, entities);
    }

    /**
     * Gets the current inventory.
     * @return the current inventory.
     */
    public Inventory getInventory() {
        this.validateSize();
        String name = this.getName() == null ? "§f" : this.getName();
        if (this.getSize() > 0 && (this.getType() == null || this.getType() == InventoryType.CHEST))
            return Bukkit.createInventory(this.getOwner(), InventoryType.CHEST, name);
        else return Bukkit.createInventory(this.getOwner(), this.getSize(), name);
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Asserts the size is a multiple of 9 and below 55.
     */
    private void validateSize() {
        if (this.getSize() < 9 || this.getSize() > 54 || this.getSize() % 9 != 0)
            throw new IllegalArgumentException("Inventory size must be a multiple of 9 between 9 and 54.");
    }
}
