package com.ankoki.roku.bukkit.misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Particles {
    private static final double _2PI = Math.PI + Math.PI;

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
                                @Nullable Particle.DustOptions options) {
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
     * Draws a circle along the X/Z axis using the given parameters.
     * @param center the center of the circle.
     * @param points the amount of particles to be used.
     * @param radius the radius of the circle.
     * @param particle the particle to spawn.
     * @param options the dust options to apply to the particle.
     */
    public static void drawHorizontalCircle(@NotNull Location center,
                                            double points,
                                            double radius,
                                            @NotNull Particle particle,
                                            @Nullable Particle.DustOptions options) {
        World world = center.getWorld();
        double increment = _2PI / points;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        for (Location location : locations) {
            if (options == null) world.spawnParticle(particle, location, 1);
            else world.spawnParticle(particle, location, 1, options);
        }
    }

    /**
     * Draws a circle along the Y axis using the given parameters.
     * @param center the center of the circle.
     * @param points the amount of particles to be used.
     * @param radius the radius of the circle.
     * @param particle the particle to spawn.
     * @param options the dust options to apply to the particle.
     */
    public static void drawVerticalCircle(@NotNull Location center,
                                          double points,
                                          double radius,
                                          @NotNull Particle particle,
                                          @Nullable Particle.DustOptions options) {
        World world = center.getWorld();
        double increment = _2PI / points;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double y = center.getY() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, y, center.getZ()));
        }
        for (Location location : locations) {
            if (options == null) world.spawnParticle(particle, location, 1);
            else world.spawnParticle(particle, location, 1, options);
        }
    }

    /**
     * Draws a torus using the given parameters.
     * @param center the center of the torus.
     * @param majorRadius the radius of the major circle.
     * @param minorRadius the radius of the minor circles.
     * @param density the density of the torus.
     * @param particle the particle to spawn.
     * @param options the dust options to apply to the particle.
     */
    public static void drawTorus(@NotNull Location center,
                                 double majorRadius,
                                 double minorRadius,
                                 double density,
                                 @NotNull Particle particle,
                                 @Nullable Particle.DustOptions options) {
        World world = center.getWorld();
        double majorCircumference = _2PI * majorRadius * density;
        double minorCircumference = _2PI * minorRadius * density;
        List<Location> points = new ArrayList<>();
        double deltaMajor = _2PI / majorCircumference;
        double deltaMinor = _2PI / minorCircumference;
        for (int i = 0; i < (int) minorCircumference; i++) {
            double cosTheta = Math.cos(i * deltaMinor), sinTheta = Math.sin(i * deltaMinor);
            for (int j = 0; j < (int) majorCircumference; j++) {
                double x = (majorRadius + minorRadius * cosTheta) * Math.cos(j * deltaMajor);
                double y = minorRadius * sinTheta;
                double z = (majorRadius + minorRadius * cosTheta) * Math.sin(j * deltaMajor);
                Location point = center.clone();
                point.add(new Vector(x, y, z));
                points.add(point);
            }
        }
        for (Location location : points) {
            if (options == null) world.spawnParticle(particle, location, 1);
            else world.spawnParticle(particle, location, 1, options);
        }
    }
}
