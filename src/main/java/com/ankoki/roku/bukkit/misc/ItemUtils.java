package com.ankoki.roku.bukkit.misc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemUtils {

    /**
     * Gets an item with the skull from a textures.minecraft link.
     * @param link the link. Has to have the format of <p>
     * 'http://textures.minecraft.net/texture/1e0a82f6e5221c6de51c4e463d915a5109e93b6b6d904bd9e72acc5cd1d0fa9e' <p>
     *             the link is optional.
     * @param amount the amount of the item.
     * @param name the name of the item.
     * @param lore the lore of the item.
     * @return the ItemStack
     */
    public static ItemStack getSkull(@NotNull String link, int amount, String name, String... lore) {
        if (!link.startsWith("http://textures.minecraft.net/texture/")) link = "http://textures.minecraft.net/texture/" + link;
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", link).getBytes());
        profile.setProperty(new ProfileProperty("textures", new String(encodedData)));
        meta.setPlayerProfile(profile);
        if (name != null) meta.setDisplayName(name);
        if (lore.length > 0) meta.setLore(List.of(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets an item with the skull from a textures.minecraft link.
     * @param link the link. Has to have the format of <p>
     * 'http://textures.minecraft.net/texture/1e0a82f6e5221c6de51c4e463d915a5109e93b6b6d904bd9e72acc5cd1d0fa9e' <p>
     *      *             the link is optional.
     * @param name the name of the item.
     * @param lore the lore of the item.
     * @return the ItemStack
     */
    public static ItemStack getSkull(@NotNull String link, String name, String... lore) {
        return ItemUtils.getSkull(link, 1, name, lore);
    }

    /**
     * Gets an item with the skull from a textures.minecraft link.
     * @param link the link. Has to have the format of <p>
     * 'http://textures.minecraft.net/texture/1e0a82f6e5221c6de51c4e463d915a5109e93b6b6d904bd9e72acc5cd1d0fa9e' <p>
     *      *             the link is optional.
     * @return the ItemStack
     */
    public static ItemStack getSkull(@NotNull String link) {
        return ItemUtils.getSkull(link, 1, null);
    }

    /**
     * Creates an ItemStack with the given name and lore.
     * @param name the name.
     * @param material the material.
     * @param amount the amount of the item.
     * @param lore the lore.
     * @return the new ItemStack.
     */
    public static ItemStack from(String name, @NotNull Material material, int amount, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name == null ? "§f" : name);
        if (lore.length > 0) meta.setLore(List.of(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates an ItemStack with the given name and lore.
     * @param name the name.
     * @param material the material.
     * @param lore the lore.
     * @return the new ItemStack.
     */
    public static ItemStack from(String name, @NotNull Material material, String... lore) {
        return ItemUtils.from(name, material, 1, lore);
    }

    /**
     * Makes an item glow.
     * TODO; Remake this to make it glow without enchantments.
     * @param stack the item to set glowing.
     * @return the glowing item.
     */
    public static ItemStack makeGlow(@NotNull ItemStack stack) {
        stack.addUnsafeEnchantment(stack.getType() == Material.BOW ?
                Enchantment.RIPTIDE : Enchantment.ARROW_INFINITE, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     *
     */
    public static ItemStack getBlank(@NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("§f");
        stack.setItemMeta(meta);
        return stack;
    }
}
