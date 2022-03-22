package com.ankoki.roku.bukkit;

import com.ankoki.roku.bukkit.advancements.Advancement;
import com.ankoki.roku.bukkit.advancements.AdvancementTrigger;
import com.ankoki.roku.bukkit.advancements.Frame;
import com.ankoki.roku.bukkit.advancements.exceptions.InvalidAdvancementException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitImpl extends JavaPlugin {

    private static BukkitImpl instance;
    private final NamespacedKey ADVANCEMENT_KEY = new NamespacedKey(this, "roku_test");

    @Override
    public void onEnable() {
        instance = this;
        BukkitImpl.info("§8- §7ROKU §8- §aENABLED §7-");
        Advancement.removeAdvancement(ADVANCEMENT_KEY, true);
        try {
            new Advancement(ADVANCEMENT_KEY)
                    .setTitle("Roku Advancement")
                    .setDescription("Roku test advancement")
                    .setFrame(Frame.CHALLENGE)
                    .setAnnounced(true)
                    .setIcon(Material.DIAMOND)
                    .setBackground("minecraft:yellow_wool")
                    .addCriteria("default", AdvancementTrigger.IMPOSSIBLE)
                    .load();
            BukkitImpl.info("Advancement loaded");
        } catch (InvalidAdvancementException ex) {
            ex.printStackTrace();
        }
        this.getServer().getPluginCommand("roku").setExecutor(this);
    }

    public static void info(String log) {
        instance.getLogger().info(log);
    }

    public static void warning(String warning) {
        instance.getLogger().warning(warning);
    }

    public static void error(String error) {
        instance.getLogger().severe(error);
    }

    public static BukkitImpl getInstance() {
        return instance;
    }

    public static UnsafeValues getUnsafe() {
        return instance.getServer().getUnsafe();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            Advancement.awardAdvancement(player, Advancement.getAdvancement(ADVANCEMENT_KEY));
            sender.sendMessage("ur welcome");
        }
        return true;
    }
}