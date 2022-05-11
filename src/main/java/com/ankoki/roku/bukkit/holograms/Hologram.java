package com.ankoki.roku.bukkit.holograms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Hologram {

    /**
     * Gets the id of the current hologram.
     * @return the id of the current hologram.
     */
    int getId();

    /**
     * Adds a line to the hologram.
     * @param line the text to add.
     */
    void addLine(String line);

    /**
     * Sets the line for the hologram
     * @param index index to set, starts at 0.
     * @param line the text to set it to.
     */
    void setLine(int index, String line);

    /**
     * Teleports the hologram to the new location.
     * @param location the new location.
     */
    void setLocation(Location location);

    /**
     * Gets all the lines of the hologram.
     * @return all lines of the current hologram.
     */
    @NotNull List<String> getLines();

    /**
     * Deletes the current hologram.
     */
    void delete();

    /**
     * Checks whether the current hologram still exists.
     * @return true if the current hologram still exists.
     */
    boolean exists();
}
