package com.ankoki.roku.bukkit.guis;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class GUIHandler implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        for (IGUI gui : GUI.getRegistry()) {
            if (event.getInventory() == gui.getInventory()) {
                if (gui instanceof PaginatedGUI paginated && current != null) {
                    if (paginated.openPage(current, entity)) break;
                }
                gui.onClick(event);
                break;
            }
        }
    }

    @EventHandler
    private void onDrag(InventoryDragEvent event) {
        for (IGUI gui : GUI.getRegistry()) {
            if (event.getInventory() == gui.getInventory()) gui.onDrag(event);
        }
    }
}
