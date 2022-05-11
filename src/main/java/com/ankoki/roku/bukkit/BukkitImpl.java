package com.ankoki.roku.bukkit;

import com.ankoki.roku.bukkit.advancements.Advancement;
import com.ankoki.roku.bukkit.advancements.AdvancementTrigger;
import com.ankoki.roku.bukkit.advancements.Background;
import com.ankoki.roku.bukkit.advancements.Frame;
import com.ankoki.roku.bukkit.advancements.exceptions.InvalidAdvancementException;
import com.ankoki.roku.bukkit.boards.Board;
import com.ankoki.roku.bukkit.guis.GUI;
import com.ankoki.roku.bukkit.guis.GUIHandler;
import com.ankoki.roku.bukkit.guis.PaginatedGUI;
import com.ankoki.roku.bukkit.misc.ItemUtils;
import com.ankoki.roku.misc.ReflectionUtils;
import com.ankoki.roku.misc.Version;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * JavaPlugin class to deal with Bukkit implementation.
 */
public class BukkitImpl extends JavaPlugin implements Listener {

    private static BukkitImpl instance;
    private final String rokuVersion = this.getDescription().getVersion();
    private final boolean dev = rokuVersion.endsWith("-dev");
    private static final String COMMAND_PREFIX = "§7§oRoku; §f";
    private final Version serverVersion = Version.of(this.getServer().getBukkitVersion().split("-")[0]);

    // DEV
    private final NamespacedKey ADVANCEMENT_KEY = new NamespacedKey(this, "roku_test");
    private final GUI TEST_GUI = new GUI("§eVoltage §7~ §cITZY", 27)
            .setShape("xxxxxxxxx", "xxxxAxxxx", "xxxxxxxxx")
            .setShapeItem('x', ItemUtils.getBlank(Material.BLACK_STAINED_GLASS_PANE))
            .setShapeItem('A', ItemUtils.getSkull("3ec6c6e00a6ad055f250546a8c0da070df4613a5f65517a9933bd5de969d8406", "§f"))
            .addClickEvent(event -> {
                event.setCancelled(true);
                HumanEntity entity = event.getWhoClicked();
                entity.sendMessage(COMMAND_PREFIX + "§9Liquid Smooth §7~ §8Mitski");
            }).setDragEvent(event -> event.setCancelled(true));
    private PaginatedGUI TEST_PAGINATED_GUI = null;

    @Override
    public void onEnable() {
        instance = this;
        ReflectionUtils.setBukkitVersion(this.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);
        ReflectionUtils.setNewNms(serverVersion.isNewerThan(1, 16));
        if (this.isDev()) {
            BukkitImpl.warning("Development build detected, if this is not intended, please report this on the github.");
            this.advancementTest();
            GUI.registerGUI(TEST_GUI);
            TEST_PAGINATED_GUI = new PaginatedGUI(new GUI("First page :)", 18)
                    .setShape("---------", "aaaaaaaaa")
                    .setShapeItem('-', ItemUtils.getBlank(Material.CHAIN))
                    .setShapeItem('a', ItemUtils.getBlank(Material.COOKIE))
                    .setSlot(2, PaginatedGUI.makeButton("next", ItemUtils.getBlank(Material.BONE))))
                    .registerPage("next", 1, new GUI("Second page!!!!!!", 9)
                            .setShape("----a----")
                            .setShapeItem('-', ItemUtils.getBlank(Material.BLACK_BED))
                            .addClickEvent(event -> event.setCancelled(true))
                            .setDragEvent(event -> event.setCancelled(true)));
            GUI.registerGUI(TEST_PAGINATED_GUI);
            BukkitImpl.info("Test GUI has been created and registered.");
            this.getServer().getPluginManager().registerEvents(this, this);
        }
        this.getServer().getPluginManager().registerEvents(new GUIHandler(), this);
        this.getServer().getPluginCommand("roku").setExecutor(this);
        BukkitImpl.info("§8- §7ROKU §8- §aENABLED §7-");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Sends an info level message to the console.
     * @param log the text.
     */
    public static void info(String log) {
        instance.getLogger().info(log);
    }

    /**
     * Sends a warning level message to the console.
     * @param warning the text.
     */
    public static void warning(String warning) {
        instance.getLogger().warning(warning);
    }

    /**
     * Sends an error level message to the console.
     * @param error the text.
     */
    public static void error(String error) {
        instance.getLogger().severe(error);
    }

    /**
     * Gets the BukkitImpl instance.
     * @return the instance.
     */
    public static BukkitImpl getInstance() {
        return instance;
    }

    /**
     * Gets the unsafe values of the bukkit server.
     * @return the unsafe.
     */
    public static UnsafeValues getUnsafe() {
        return instance.getServer().getUnsafe();
    }

    /**
     * Whether Roku is on a development version.
     * @return true if Roku version ends with -dev.
     */
    public boolean isDev() {
        return dev;
    }

    /**
     * Gets the current server version.
     * @return the current server version.
     */
    public Version getServerVersion() {
        return serverVersion;
    }


    private void advancementTest() {
        try {
            // Advancements are persistent once registered, so only
            // register a new one if it doesn't exist!
            if (!Advancement.advancementExists(ADVANCEMENT_KEY))
                new Advancement(ADVANCEMENT_KEY)
                        .setTitle("§7§oRoku Development Build")
                        .setDescription("§fYou used a development build of §7§oRoku§f!")
                        .setFrame(Frame.CHALLENGE)
                        .setAnnounced(true)
                        .setIcon(Material.DIAMOND)
                        .setBackground(Background.END)
                        .addCriteria("default", AdvancementTrigger.IMPOSSIBLE)
                        .load();
            BukkitImpl.info("Advancement loaded");
        } catch (InvalidAdvancementException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Board board = Board.of(event.getPlayer());
        board.setTitle("§7§lROKU §f• §a§oDEVELOPMENT");
        board.setLine(1, "§c • Bottom •");
        board.setLine(2, "§f");
        board.setLine(3, "§e • Top •");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (this.isDev()) {
            if (sender instanceof Player player && args.length == 1) {
                switch (args[0].toUpperCase()) {
                    case "ADVGIVE" -> Advancement.awardAdvancement(player, Advancement.getAdvancement(ADVANCEMENT_KEY));
                    case "ADVREVOKE" -> Advancement.revokeAdvancement(player, Advancement.getAdvancement(ADVANCEMENT_KEY));
                    case "GUI" -> TEST_GUI.openTo(player);
                    case "PAGINATEDGUI" -> TEST_PAGINATED_GUI.openTo(player);
                }
                sender.sendMessage(COMMAND_PREFIX + "done something with the arg " + args[0]);
            }
        } else sender.sendMessage(COMMAND_PREFIX + "Thank you for using Roku v" + rokuVersion);
        return true;
    }
}