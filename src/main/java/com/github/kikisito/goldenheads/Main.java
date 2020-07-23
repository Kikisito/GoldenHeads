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
import com.github.kikisito.goldenheads.listeners.BlockPlaceListener;
import com.github.kikisito.goldenheads.listeners.PlayerDeathListener;
import com.github.kikisito.goldenheads.listeners.PlayerInteractListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private NamespacedKey recipe;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        GHConfig.setConfig(this.getConfig());
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        if(GHConfig.DROP_PLAYER_HEAD_ON_DEATH.getBoolean()) this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        this.registerRecipe();
        Metrics metrics = new Metrics(this, 8284);
    }

    @Override
    public void onDisable() {
        this.getServer().removeRecipe(recipe);
    }

    public void registerRecipe(){
        ItemStack goldenhead = GoldenHead.createHead(this);
        // Recipe
        recipe = new NamespacedKey(this, "golden_head");
        ShapedRecipe shapedRecipe = new ShapedRecipe(recipe, goldenhead);
        shapedRecipe.shape("GGG", "GPG", "GGG");
        shapedRecipe.setIngredient('G', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('P', Material.PLAYER_HEAD);
        this.getServer().addRecipe(shapedRecipe);
    }
}
