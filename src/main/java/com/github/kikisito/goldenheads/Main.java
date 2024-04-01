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
import com.github.kikisito.goldenheads.listeners.BlockPlaceListener;
import com.github.kikisito.goldenheads.listeners.PlayerDeathListener;
import com.github.kikisito.goldenheads.listeners.PlayerInteractListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private NamespacedKey recipe;
    private Logger logger;
    private ConfigMapper configMapper;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new GoldenHeadsModule(this));
        logger = injector.getInstance(Logger.class);
        configMapper = injector.getInstance(ConfigMapper.class);

        ConfigurationContainer<Config> configContainer = injector.getInstance(new Key<ConfigurationContainer<Config>>() {});
        configMapper.register(Config.class, configContainer);

        Config config = configContainer.get();

        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);

        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, configMapper), this);

        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, configMapper), this);

        this.registerRecipe();

        Metrics metrics = new Metrics(this, 8284);
    }

    @Override
    public void onDisable() {
        if (recipe != null) {
            this.getServer().removeRecipe(recipe);
        }
    }

    public void registerRecipe() {
        GoldenHead goldenHead = new GoldenHead(configMapper);
        ItemStack goldenhead = goldenHead.createHead(this, java.util.Optional.empty());
        // Recipe
        recipe = new NamespacedKey(this, "golden_head");
        ShapedRecipe shapedRecipe = new ShapedRecipe(recipe, goldenhead);
        shapedRecipe.shape("GGG", "GPG", "GGG");
        shapedRecipe.setIngredient('G', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('P', Material.PLAYER_HEAD);
        this.getServer().addRecipe(shapedRecipe);
    }
}
