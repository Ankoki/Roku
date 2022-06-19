package com.ankoki.roku.bukkit.guis.events;

import org.bukkit.event.inventory.InventoryDragEvent;

public interface DragEvent {

    /**
     * Called on an InventoryDragEvent and is used in {@link com.ankoki.roku.bukkit.guis.GUI} for simplicity.
     * @param event the passed event.
     */
    void onDrag(InventoryDragEvent event);
}
