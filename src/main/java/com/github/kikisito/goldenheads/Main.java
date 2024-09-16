package com.github.kikisito.goldenheads;

import com.github.kikisito.goldenheads.commands.GoldenHeads;
import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import com.github.kikisito.goldenheads.listeners.BlockPlaceListener;
import com.github.kikisito.goldenheads.listeners.PlayerDeathListener;
import com.github.kikisito.goldenheads.listeners.PlayerInteractListener;
import com.github.kikisito.goldenheads.listeners.PrepareCraftListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public final class Main extends JavaPlugin {
    private NamespacedKey recipe;
    private Logger logger;
    private ConfigMapper configMapper;
    private GoldenHeads goldenHeadsCommand;
    private VersionController versionController;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new GoldenHeadsModule(this));
        logger = injector.getInstance(Logger.class);
        logger.info("Thanks for using GoldenHeads v" + getDescription().getVersion() + " by Kikisito.");
        configMapper = injector.getInstance(ConfigMapper.class);

        versionController = injector.getInstance(VersionController.class);


        // Register the configuration
        ConfigurationContainer<Config> configContainer = injector.getInstance(new Key<ConfigurationContainer<Config>>() {});
        configMapper.register(Config.class, configContainer);

        Config config = configContainer.get();

        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this, logger), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, configMapper, logger), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, configMapper, logger), this);
        this.getServer().getPluginManager().registerEvents(new PrepareCraftListener(this, configMapper, logger), this);

        this.registerRecipe();

        goldenHeadsCommand = injector.getInstance(GoldenHeads.class);
        getCommand("goldenheads").setExecutor(goldenHeadsCommand);

        if (config.getDebug()) {
            logger.info("Debug mode enabled, if you are not a developer, please disable it in the configuration file.");
        }

        if (isFolia()) {
            logger.info("Folia is enabled, delaying potion effects is not supported.");
        }

        if (Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null) {
            logger.warn("It seems like you are using Geyser, please note that you will need to set add-non-bedrock-items to true in the Geyser config to make GoldenHeads look like they should. (If you are using heads, please add them to the file custom-skulls.yml in the Geyser plugin folder)");
        }

        checkVersion();
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

        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));
        Config config = configContainer.get();

        shapedRecipe.shape(config.recipe.getShape().toArray(new String[0]));

        for (Map.Entry<Character, String> entry : config.recipe.getIngredients().entrySet()) {
            shapedRecipe.setIngredient(entry.getKey(), Material.valueOf(entry.getValue()));
        }

        this.getServer().addRecipe(shapedRecipe);
    }

    public void reloadConfig() {
        logger.info("Reloading GoldenHeads...");
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        configContainer.reload()
                .exceptionally(e -> {
                    logger.error("Failed to reload configuration: " + e.getMessage());
                    return null;
                });
        logger.info("Configuration values updated.");
        if (!isFolia()) {
            getServer().getScheduler().runTask(this, () -> {
                // Remove the old recipe
                if (recipe != null) {
                    logger.debug("Removing old recipe...");
                    this.getServer().removeRecipe(recipe);
                } else {
                    logger.debug("Recipe not found, skipping removal.");
                }

                // Register the recipe again with the updated configuration
                logger.debug("Registering recipe with updated configuration...");
                this.registerRecipe();
                logger.info("Recipe updated.");
            });
        } else {
            logger.warn("Folia is enabled, recipe will not be reloaded.");
        }
        logger.info("GoldenHeads reloaded successfully.");
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private void checkVersion() {
        PluginDescriptionFile pluginDescription = getDescription();
        String currentVersion = pluginDescription.getVersion();

        String latestTag = versionController.getLatestTagFromGitHub();

        if (latestTag != null) {
            if (versionController.compareVersions(currentVersion, latestTag) < 0) {
                logger.warn("You are <red>not</red> using the latest version, please consider updating to v" + latestTag);

            } else {
                logger.info("The plugin is up to date!");
            }
        } else {
            logger.error("There was an error while contacting GitHub, could not check the latest version.");
        }
    }

    public boolean isOutdated() {
        // Use CompareVersions method to check if the plugin is outdated, among with the latest version from GitHub
        return versionController.compareVersions(getDescription().getVersion(), Objects.requireNonNull(versionController.getLatestTagFromGitHub())) < 0;
    }
}