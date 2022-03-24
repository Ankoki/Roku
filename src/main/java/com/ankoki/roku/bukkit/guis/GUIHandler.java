package com.ankoki.roku.bukkit.guis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GUIHandler implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        for (IGUI gui : GUI.getRegistry()) {
            gui.onClick(event);
        }
    }

    @EventHandler
    private void onDrag(InventoryDragEvent event) {
        for (IGUI gui : GUI.getRegistry()) {
            gui.onDrag(event);
        }
    }
}
