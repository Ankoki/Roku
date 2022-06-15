package com.ankoki.roku.bukkit.misc;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitMisc {

    private static Pattern HEX_PATTERN = Pattern.compile("<#([a-fA-F\\d]{6})>");

    /**
     * Colours a text.
     * @param text the text to colour.
     * @return the coloured text.
     */
    public static String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Colours a text based on the current hex pattern. {@link BukkitMisc#setHexPattern(String)}
     * @param text the text to colour.
     * @return the coloured text.
     */
    public static String colourHex(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        if (matcher.find()) {
            /*
            StringBuilder current = new StringBuilder();
            while (matcher.find(0)) {
                StringBuilder builder = new StringBuilder(matcher.group(1));
                for (int i = 0; i <= 11; i += 2) builder.insert(i, "§");
                matcher.appendReplacement(current, builder.toString());
            } return BukkitMisc.colour(current.toString());
            */
            String matched = matcher.group(1);
            // TODO change the way this works, as custom patterns will not support <# >
            text = text.replace("<#" + matched + ">", net.md_5.bungee.api.ChatColor.of("#" + matched).toString());

        } return BukkitMisc.colour(text);
    }

    /**
     * Sets a new pattern to find hex.
     * By default; it is '&lt;#([a-fA-F\d]{6})&gt;', meaning &lt;#hexcode&gt; would be applicable.
     * Ensure that the hex is found in the regex group 1 and does not contain the # or any other character.
     * Please remember the {@link Pattern#compile(String)} call is intensive, so do not use this carelessly.
     * @param hexPattern the new regex hex pattern.
     */
    public static void setHexPattern(String hexPattern) {
        HEX_PATTERN = Pattern.compile(hexPattern);
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
