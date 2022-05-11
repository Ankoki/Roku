package com.ankoki.roku.bukkit.holograms;

import com.ankoki.roku.misc.ReflectionUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class HologramImpl implements Hologram {

    private static final Class<?> ARMOUR_STAND = ReflectionUtils.getNMSClass("net.minecraft.world.entity.decoration", "ArmorStand");
    private static final Class<?> SPAWN_PACKET = ReflectionUtils.getNMSClass("network.protocol.game", "PacketPlayOutSpawnEntityLiving");
    private static final Class<?> METADATA_PACKET = ReflectionUtils.getNMSClass("network.protocol.game", "PacketPlayOutEntityMetadata");
    private static final Class<?> TELEPORT_PACKET = ReflectionUtils.getNMSClass("network.protocol.game", "PacketPlayOutEntityTeleport");
    private static final Class<?> DESTROY_PACKET = ReflectionUtils.getNMSClass("network.protocol.game", "PacketPlayOutEntityDestroy");

    private static final Constructor<?> ARMOUR_STAND_CONSTRUCTOR = ReflectionUtils.getConstructor(ARMOUR_STAND, double.class, double.class, double.class);

    private static final Method IS_ALIVE_METHOD = ReflectionUtils.getNMSMethod(ARMOUR_STAND, "isDeadOrDying", "dV");
    private static final Method KILL_METHOD = ReflectionUtils.getNMSMethod(ARMOUR_STAND, "kill", "ab");
    private static final Method INVISIBLE_METHOD = ReflectionUtils.getNMSMethod(ARMOUR_STAND, "setInvisible", "j");

    private final List<String> lines = new ArrayList<>();
    private final Object armourStand;
    private final int id;

    protected HologramImpl(int id, Location location, String... lines) {
        this.id = id;
        armourStand = ReflectionUtils.invokeConstructor(ARMOUR_STAND_CONSTRUCTOR, location.getX(), location.getY(), location.getZ());
        ReflectionUtils.invokeInstanceMethod(INVISIBLE_METHOD, armourStand, true);

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addLine(String line) {

    }

    @Override
    public void setLine(int index, String line) {

    }

    @Override
    public void setLocation(Location location) {

    }

    @Override
    public @NotNull List<String> getLines() {
        return lines;
    }

    @Override
    public void delete() {
        ReflectionUtils.invokeInstanceMethod(KILL_METHOD, armourStand);
    }

    @Override
    public boolean exists() {
        return (boolean) ReflectionUtils.invokeMethod(IS_ALIVE_METHOD, armourStand);
    }
}
