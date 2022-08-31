package com.ankoki.roku.bukkit.guis.events;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CloseEvent {

    /**
     * Called on an InventoryCloseEvent and is used in {@link com.ankoki.roku.bukkit.guis.GUI} for simplicity.
     * @param event the passed event.
     */
    void onClose(InventoryCloseEvent event);
}
