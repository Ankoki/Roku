package com.ankoki.roku.bukkit.misc;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class BukkitMisc {

    /**
     * Colours a text.
     * @param text the text to colour.
     * @return the coloured text.
     */
    public static String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Gets a CompletableFuture which contains an image of the given player's face.
     * @param player the player to get the face of.
     * @return the Image.
     */
    public static CompletableFuture<Image> getFace(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ImageIO.read(new URL("https://craftcrafatar.com/avatars/" + player.getUniqueId() + "/8"));
            } catch (IOException ex) {
                ex.printStackTrace();
            } return null;
        });
    }
}
