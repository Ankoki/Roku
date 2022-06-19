package com.ankoki.roku.bukkit.guis.events;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickEvent {

    /**
     * Called on an InventoryClickEvent and is used in {@link com.ankoki.roku.bukkit.guis.GUI} for simplicity.
     * @param event the passed event.
     */
    void onClick(InventoryClickEvent event);
}
