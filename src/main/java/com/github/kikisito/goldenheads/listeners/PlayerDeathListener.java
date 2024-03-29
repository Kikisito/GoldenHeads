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

package com.github.kikisito.goldenheads.listeners;

import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.enums.GHConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerDeathListener implements Listener {
    private Main plugin;

    public PlayerDeathListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(PlayerDeathEvent e){
        if(!GHConfig.DROP_PLAYER_HEAD_ONLY_WHEN_KILLED_BY_A_PLAYER.getBoolean() || e.getEntity().getKiller() != null){
            ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta phmeta = playerhead.getItemMeta();
            ((SkullMeta) phmeta).setOwningPlayer(plugin.getServer().getOfflinePlayer(e.getEntity().getUniqueId()));
            playerhead.setItemMeta(phmeta);
            e.getDrops().add(playerhead);
        }
    }
}
