package com.ankoki.roku.bukkit.misc;

import org.bukkit.ChatColor;

public class BukkitMisc {

    /**
     * Colours a text.
     * @param text the text to colour.
     * @return the coloured text.
     */
    public static String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
