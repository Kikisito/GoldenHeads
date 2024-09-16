package com.github.kikisito.goldenheads;

import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import com.google.inject.Inject;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GoldenHead {
    private static ConfigMapper configMapper;

    @Inject
    public GoldenHead(ConfigMapper configMapper) {
        GoldenHead.configMapper = configMapper;
    }

    public static ItemStack createHead(Main plugin, Optional<Player> player) {
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        Config config = configContainer.get();

        // Set material
        ItemStack goldenhead = new ItemStack(Material.valueOf(config.goldenHeads.getMaterial()));
        ItemMeta itemMeta = goldenhead.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        assert itemMeta != null;
        itemMeta.setCustomModelData(config.goldenHeads.getCustomModelData());
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "GoldenHead");

        // Set owner
        if (goldenhead.getType() == Material.PLAYER_HEAD) {
            if (player.isPresent()) {
                ((SkullMeta) itemMeta).setOwningPlayer(player.get());
            } else {
                String skullTexture = config.goldenHeads.getSkullTexture();
                if (skullTexture.length() == 36) {
                    try {
                        UUID uuid = UUID.fromString(skullTexture);
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) {
                            ((SkullMeta) itemMeta).setOwningPlayer(offlinePlayer);
                        } else {
                            plugin.getLogger().warning("No player found for UUID: " + skullTexture);
                        }
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().severe("Invalid UUID format: " + skullTexture);
                    }
                } else if (skullTexture.length() > 22) {
                    itemMeta = base64SkullBuilder((SkullMeta) itemMeta, skullTexture);
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(skullTexture);
                    if (offlinePlayer.getName() != null) {
                        ((SkullMeta) itemMeta).setOwningPlayer(offlinePlayer);
                    } else {
                        plugin.getLogger().warning("Invalid player name: " + skullTexture);
                        throw new IllegalArgumentException("Invalid player name: " + skullTexture);
                    }
                }
            }
        }

        // Set display name
        itemMeta.setDisplayName(LegacyComponentSerializer.legacySection().serialize(ColorTranslator.translate(config.goldenHeads.getDisplayName())));

        // Set lore
        List<String> finalLore = config.goldenHeads.getLore().stream()
                .map(line -> LegacyComponentSerializer.legacySection().serialize(ColorTranslator.translate(line)))
                .collect(Collectors.toList());
        itemMeta.setLore(finalLore);
        goldenhead.setItemMeta(itemMeta);
        return goldenhead;
    }

    public static SkullMeta base64SkullBuilder(SkullMeta meta, String base64) {
        if (meta == null || base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Meta and base64 must not be null or empty");
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), "CustomHead");
        profile.getProperties().put("textures", new Property("textures", base64));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return meta;
    }

    public static boolean isGoldenHead(Main plugin, ItemStack item) {
        if (item == null) return false;
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}