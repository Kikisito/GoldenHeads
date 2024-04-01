/*
 * Copyright (C) 2020  Kikisito (Kyllian)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.kikisito.goldenheads;

import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class GoldenHead {
    private static ConfigMapper configMapper;

    @Inject
    public GoldenHead(ConfigMapper configMapper) {
        this.configMapper = configMapper;
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
        itemMeta.setDisplayName(ColorTranslator.translate(config.goldenHeads.getDisplayName()).toString());
        // Set lore
        List<String> finalLore = new ArrayList<>();
        for(String s : config.goldenHeads.getLore()) {
            finalLore.add(ColorTranslator.translate(s).toString());
        }
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
