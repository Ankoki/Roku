package com.ankoki.roku.bukkit.misc;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Particles {

    /**
     * Draws a line of particles between two points with the given space, particle and dust options.
     * @param pointOne the starting point.
     * @param pointTwo the finishing point.
     * @param space the space between each particle.
     * @param particle the particle to spawn.
     * @param options the dust options to apply to the particle.
     */
    public static void drawLine(@NotNull Location pointOne,
                                @NotNull Location pointTwo,
                                double space,
                                @NotNull Particle particle,
                                Particle.DustOptions options) {
        World world = pointOne.getWorld();
        if (pointTwo.getWorld() == world) throw new IllegalArgumentException("Points cannot be in different worlds.");
        double distance = pointOne.distance(pointTwo);
        Vector vectorOne = pointOne.toVector();
        Vector vectorTwo = pointTwo.toVector();
        Vector vector = vectorTwo.clone().subtract(vectorOne).normalize().multiply(space);
        for (double i = 0; i < distance; vectorOne.add(vector)) {
            if (options == null) world.spawnParticle(particle, vectorOne.getX(), vectorOne.getY(), vectorOne.getZ(), 1);
            else world.spawnParticle(particle, vectorOne.getX(), vectorOne.getY(), vectorOne.getZ(), 1, options);
            i += space;
        }
    }

    /**
     * Draws a line with the specified particle.
     * @param pointOne the starting point.
     * @param pointTwo the finishing point.
     * @param space the space between each particle.
     * @param particle the particle to spawn.
     * @param colour if the particle is redstone, will be the colour.
     * @param size if the particle is redstone, will be the size.
     */
    public static void drawLine(@NotNull Location pointOne,
                                 @NotNull Location pointTwo,
                                 double space,
                                 @NotNull Particle particle,
                                 Color colour,
                                 float size) {
        if (particle == Particle.REDSTONE) Particles.drawLine(pointOne, pointTwo, space, particle, new Particle.DustOptions(colour, size));
        else Particles.drawLine(pointOne, pointTwo, space, particle, null);
    }

    /**
     * Draws a line of redstone dust in the specified colour.
     * @param pointOne the starting point.
     * @param pointTwo the finishing point.
     * @param space the space between each particle.
     * @param colour the color to spawn.
     */
    public static void drawLine(@NotNull Location pointOne,
                                @NotNull Location pointTwo,
                                double space,
                                Color colour) {
        Particles.drawLine(pointOne, pointTwo, space, Particle.REDSTONE, colour, 1);
    }

    /**
     * Draws a line with the specified particle.
     * @param pointOne the starting point.
     * @param pointTwo the finishing point.
     * @param space the space between each particle.
     * @param particle the particle to spawn.
     */
    public static void drawLine(@NotNull Location pointOne,
                                @NotNull Location pointTwo,
                                double space,
                                @NotNull Particle particle) {
        Particles.drawLine(pointOne, pointTwo, space, particle, null, 1);
    }
}
