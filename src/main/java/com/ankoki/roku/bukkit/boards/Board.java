package com.ankoki.roku.bukkit.boards;

import com.ankoki.roku.bukkit.misc.BukkitMisc;
import com.ankoki.roku.misc.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to easily deal with giving and removing scoreboards from players.
 */
public class Board {

    private static final Map<Player, Board> REGISTERED_BOARDS = new ConcurrentHashMap<>();
    private static final String[] DEFAULT_ENTRIES = new String[]{"§a§r", "§b§r", "§c§r", "§d§r", "§e§r", "§f§r", "§0§r", "§1§r", "§2§r", "§3§r", "§4§r", "§5§r", "§6§r", "§7§r", "§8§r"};

    /**
     * Gets a board of a player.
     * @param player the player to get the board of.
     * @return player's board.
     */
    public static Board of(Player player) {
        return Board.of(player, true);
    }

    public static Board of(Player player, boolean show) {
        if (REGISTERED_BOARDS.containsKey(player)) return REGISTERED_BOARDS.get(player);
        else return new Board(player, show);
    }

    /**
     * Stops showing a board and unregisters it.
     * @param board the board to unregister.
     * @return if the board was successfully unregistered.
     */
    public static boolean unregister(Board board) {
        for (Map.Entry<Player, Board> entry : REGISTERED_BOARDS.entrySet()) {
            if (entry.getValue() == board) {
                board.setVisibility(false);
                REGISTERED_BOARDS.remove(entry.getKey());
                return true;
            }
        } return false;
    }

    private final Player player;
    private final Scoreboard previous, current;
    private final Objective board;
    private final List<Team> lines = new ArrayList<>();

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Creates a blank scoreboard for the player.
     * @param player the owning player.
     */
    private Board(Player player, boolean show) {
        this.player = player;
        previous = player.getScoreboard();
        current = Bukkit.getScoreboardManager().getNewScoreboard();
        if (show) this.player.setScoreboard(current);
        board = current.registerNewObjective("RokuBoard", "default", "RokuBoard");
        board.setDisplaySlot(DisplaySlot.SIDEBAR);
        board.setDisplayName("§7");
        Arrays.stream(NumberUtils.range(1, 15)).forEach(i -> {
            Team team = current.registerNewTeam("line-" + i);
            team.addEntry(DEFAULT_ENTRIES[i - 1]);
            team.setSuffix("");
            lines.add(team);
        });
        REGISTERED_BOARDS.put(player, this);
    }

    /**
     * Sets the title of the scoreboard.
     * @param title the title.
     */
    public void setTitle(@NotNull String title) {
        title = BukkitMisc.colour(title);
        if (ChatColor.stripColor(title).length() > 128) throw new IllegalArgumentException("Paramater 'title' cant be more than 128 characters.");
        board.setDisplayName(title);
    }

    /**
     * Gets the contents of a line.
     * @param line the line to get the contents of.
     * @return the contents of given line.
     */
    public String getLine(int line) {
        if (!NumberUtils.inRange(line, 1, 15)) throw new IllegalArgumentException("Parameter 'line' must be between 1 and 15.");
        Team team = lines.get(line - 1);
        return team.getPrefix();
    }

    /**
     * Sets a line for the scoreboard.
     * @param line the line to set. Must be between 1 and 15.
     * @param content the contents to set it too. Cannot be more than 64.
     */
    public void setLine(int line, @NotNull String content) {
        if (!NumberUtils.inRange(line, 1, 15)) throw new IllegalArgumentException("Parameter 'line' must be between 1 and 15.");
        Team team = lines.get(line - 1);
        content = BukkitMisc.colour(content);
        if (ChatColor.stripColor(content).length() > 64) throw new IllegalArgumentException("You cannot have a text with more than 64 characters.");
        team.setPrefix(content);
        board.getScore(DEFAULT_ENTRIES[line - 1]).setScore(line);
    }

    /**
     * Deletes a line from the scoreboard.
     * @param line the line to delete.
     */
    public void deleteLine(int line) {
        if (!NumberUtils.inRange(line, 1, 15)) throw new IllegalArgumentException("Parameter 'line' must be between 1 and 15.");
        current.resetScores(DEFAULT_ENTRIES[line - 1]);
    }

    /**
     * Clears all lines of the scoreboard.
     */
    public void clearLines() {
        for (int i : NumberUtils.range(0, 14)) {
            current.resetScores(DEFAULT_ENTRIES[i]);;
        }
    }

    /**
     * Shows or hides the board.
     * @param visibility true if you want to show, otherwise false.
     */
    public void setVisibility(boolean visibility) {
        player.setScoreboard(visibility ? current : previous);
    }

    /**
     * Toggles the board's visibility.
     */
    public void toggle() {
        if (player.getScoreboard() == current) player.setScoreboard(previous);
        else player.setScoreboard(current);
    }

    /**
     * Whether the board is being shown to the player.
     * @return true if showing, otherwise false.
     */
    public boolean isShowing() {
        return player.getScoreboard() == current;
    }

    /**
     * Gets the owning player of this board.
     * @return the owner.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Stops showing and unregisters a board.
     * @return true if the board was successfully unregistered.
     */
    public boolean unregister() {
        return Board.unregister(this);
    }
}
