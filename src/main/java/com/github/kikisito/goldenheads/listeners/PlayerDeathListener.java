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

import com.github.kikisito.goldenheads.Logger;
import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerDeathListener implements Listener {
    private Main plugin;
    private static ConfigMapper configMapper;
    private Logger logger;

    public PlayerDeathListener(Main plugin, ConfigMapper configMapper, Logger logger) {
        this.plugin = plugin;
        this.configMapper = configMapper;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        logger.debug("Detected player death event.");
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        Config config = configContainer.get();
        if(!config.getDropHeadOnlyWhenKilledByAPlayer() || e.getEntity().getKiller() != null){
            logger.debug("Dropped head.");
            ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta phmeta = playerhead.getItemMeta();
            ((SkullMeta) phmeta).setOwningPlayer(plugin.getServer().getOfflinePlayer(e.getEntity().getUniqueId()));
            playerhead.setItemMeta(phmeta);
            e.getDrops().add(playerhead);
        }
    }
}
