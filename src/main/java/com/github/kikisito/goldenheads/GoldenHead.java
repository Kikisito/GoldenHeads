package com.github.kikisito.goldenheads;

import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class GoldenHead {
    private static ConfigMapper configMapper;

    @Inject
    public GoldenHead(ConfigMapper configMapper) {
        GoldenHead.configMapper = configMapper;
    }

    public static ItemStack createHead(Main plugin, Optional<Player> player){
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        Config config = configContainer.get();

        // Set material
        ItemStack goldenhead = new ItemStack(Material.valueOf(config.goldenHeads.getMaterial()));
        ItemMeta itemMeta = goldenhead.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        Objects.requireNonNull(itemMeta).getPersistentDataContainer().set(key, PersistentDataType.STRING, "GoldenHead");
        // Set owner
        if(goldenhead.getType() == Material.PLAYER_HEAD){
            if(player.isPresent()){
                ((SkullMeta) itemMeta).setOwningPlayer(player.get());
            } else {
                ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(config.goldenHeads.getSkullOwner()));
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

    public static boolean isGoldenHead(Main plugin, ItemStack item){
        if(item == null) return false;
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
