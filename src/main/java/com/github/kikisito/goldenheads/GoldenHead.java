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

import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
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
                // Never called
                ((SkullMeta) itemMeta).setOwningPlayer(player.get());
            } else {
                String skullTexture = config.goldenHeads.getSkullTexture();
                if (skullTexture.length() == 36) {
                    // skullTexture is an UUID
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
                    // skullTexture is an URL
                    itemMeta = skullBuilder((SkullMeta) itemMeta, skullTexture);
                } else {
                    // skullTexture is a player name (should not be used)
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

    public static SkullMeta skullBuilder(SkullMeta meta, String texturesUrl) {
        if (meta == null || texturesUrl == null || texturesUrl.isEmpty()) {
            throw new IllegalArgumentException("Meta and texture url must not be null or empty");
        }

        // PlayerProfile uuid is based on the texture url.
        // If the texture changes, items won't be stackable anyway so this should not be a problem
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(texturesUrl.getBytes()));
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URL(texturesUrl));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid texture url: " + texturesUrl);
        }

        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        return meta;
    }

    public static boolean isGoldenHead(Main plugin, ItemStack item) {
        if (item == null || item.getItemMeta() == null) return false;
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}