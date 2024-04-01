package com.github.kikisito.goldenheads;

import com.github.kikisito.goldenheads.commands.GoldenHeads;
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
    private GoldenHeads goldenHeadsCommand;

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

        goldenHeadsCommand = injector.getInstance(GoldenHeads.class);
        getCommand("goldenheads").setExecutor(goldenHeadsCommand);

        Metrics metrics = new Metrics(this, 8284);
    }

    @Override
    public void onDisable() {
        if (recipe != null) {
            this.getServer().removeRecipe(recipe);
        }
        getCommand("goldenheads").setExecutor(null);
    }

    public void registerRecipe() {
        GoldenHead goldenHead = new GoldenHead(configMapper);
        ItemStack goldenhead = goldenHead.createHead(this, java.util.Optional.empty());
        recipe = new NamespacedKey(this, "golden_head");
        ShapedRecipe shapedRecipe = new ShapedRecipe(recipe, goldenhead);
        shapedRecipe.shape("GGG", "GPG", "GGG");
        shapedRecipe.setIngredient('G', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('P', Material.PLAYER_HEAD);
        this.getServer().addRecipe(shapedRecipe);
    }

    public void reloadConfig() {
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        configContainer.reload()
                .exceptionally(e -> {
                    logger.error("Failed to reload configuration: " + e.getMessage());
                    return null;
                });
    }
}