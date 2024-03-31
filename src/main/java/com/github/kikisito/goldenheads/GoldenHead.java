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

import com.github.kikisito.goldenheads.enums.GHConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GoldenHead {
    @SuppressWarnings("deprecation")
    public static ItemStack createHead(Main plugin, Optional<Player> player){
        // Set material
        ItemStack goldenhead = new ItemStack(Material.valueOf(GHConfig.GOLDENHEADS_MATERIAL.getString()));
        ItemMeta itemMeta = goldenhead.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "GoldenHead");
        // Set owner
        if(goldenhead.getType() == Material.PLAYER_HEAD){
            if(player.isPresent()){
                ((SkullMeta) itemMeta).setOwningPlayer(player.get());
            } else {
                ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(GHConfig.GOLDENHEADS_SKULL_OWNER.getString()));
            }
        }
        // Set display name
        itemMeta.setDisplayName(Utils.parseMessage(GHConfig.GOLDENHEADS_NAME.getString()));
        // Set lore
        List<String> finallore = new ArrayList<>();
        for(String s : GHConfig.GOLDENHEADS_LORE.getList()) finallore.add(Utils.parseMessage(s));
        itemMeta.setLore(finallore);
        goldenhead.setItemMeta(itemMeta);
        return goldenhead;
    }

    public static boolean isGoldenHead(Main plugin, ItemStack item){
        if(item == null) return false;
        NamespacedKey key = new NamespacedKey(plugin, "golden_head");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
