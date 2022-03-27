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
     * Gets the inventory.
     * @return the inventory of the GUI.
     */
    public abstract Inventory getInventory();

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
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Asserts the size is a multiple of 9 and below 55.
     */
    protected void validateSize(int size) {
        if (size < 9 || size > 54 || size % 9 != 0)
            throw new IllegalArgumentException("Inventory size must be a multiple of 9 between 9 and 54.");
    }
}
