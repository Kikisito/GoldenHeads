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

import com.github.kikisito.goldenheads.GoldenHead;
import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import com.google.inject.Inject;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerInteractListener implements Listener {
    private final Main plugin;
    private static ConfigMapper configMapper;

    @Inject
    public PlayerInteractListener(Main plugin, ConfigMapper configMapper) {
        this.plugin = plugin;
        this.configMapper = configMapper;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        Config config = configContainer.get();

        if (GoldenHead.isGoldenHead(plugin, e.getItem()) && e.getAction().toString().startsWith("RIGHT_CLICK")) {
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            Player player = e.getPlayer();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 100, 1);

            // Add potion effects
            List<String> effects = config.goldenHeads.getPotionEffects();
            for (String effect : effects) {
                String[] effect_info = effect.split("\\|");
                String effect_name = effect_info[0];
                int effect_duration = Integer.parseInt(effect_info[1]) * 20;
                int effect_level = Integer.parseInt(effect_info[2]) - 1;
                player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect_name), effect_duration, effect_level));
            }

            // Adjust food level
            int playerFood = player.getFoodLevel();
            int addFood = config.goldenHeads.getFoodAmount();
            if (playerFood + addFood > 20) player.setFoodLevel(20);
            else player.setFoodLevel(playerFood + addFood);

            // Adjust saturation
            float playerSaturation = player.getSaturation();
            double addSaturation = config.goldenHeads.getSaturationAmount();
            if (playerSaturation + addSaturation > player.getFoodLevel())
                player.setSaturation(player.getFoodLevel());
            else player.setSaturation((float) (playerSaturation + addSaturation));

            // Adjust exhaustion
            player.setExhaustion(0);
        }
    }
}
