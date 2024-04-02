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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public final class Main extends JavaPlugin {
    private NamespacedKey recipe;
    private Logger logger;
    private ConfigMapper configMapper;
    private GoldenHeads goldenHeadsCommand;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new GoldenHeadsModule(this));
        logger = injector.getInstance(Logger.class);
        logger.info("Thanks for using GoldenHeads v"+getDescription().getVersion()+" by Kikisito.");
        configMapper = injector.getInstance(ConfigMapper.class);

        ConfigurationContainer<Config> configContainer = injector.getInstance(new Key<ConfigurationContainer<Config>>() {});
        configMapper.register(Config.class, configContainer);

        Config config = configContainer.get();

        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this, logger), this);

        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, configMapper, logger), this);

        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, configMapper, logger), this);

        this.registerRecipe();

        goldenHeadsCommand = injector.getInstance(GoldenHeads.class);
        getCommand("goldenheads").setExecutor(goldenHeadsCommand);

        if (config.getDebug()) {
            logger.info("Debug mode enabled, if you are not a developer, please disable it in the configuration file.");
        }

        if (isFolia()) {
            logger.info("Folia is enabled, delaying potion effects is not supported.");
        }

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
        ConfigurationContainer<Config> configContainer = configMapper.get(Config.class)
                .orElseThrow(() -> new IllegalStateException("Config not registered in ConfigMapper"));

        configContainer.reload()
                .exceptionally(e -> {
                    logger.error("Failed to reload configuration: " + e.getMessage());
                    return null;
                });
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

        String latestTag = getLatestTagFromGitHub();

        if (latestTag != null) {
            if (compareVersions(currentVersion, latestTag) < 0) {
                logger.warn("Your <red>not</red> using the latest version, please consider updating to v" + latestTag);

            } else {
                logger.info("The plugin is up to date!");
            }
        } else {
            logger.error("There was an error while contacting GitHub, could not check the latest version.");
        }
    }

    private String getLatestTagFromGitHub() {
        try {
            URL url = new URL("https://api.github.com/repos/Kikisito/GoldenHeads/releases/latest");
            InputStream inputStream = url.openStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            String latestTag = response.split("\"tag_name\":\"")[1].split("\"")[0];
            return latestTag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (part1 < part2) {
                return -1;
            } else if (part1 > part2) {
                return 1;
            }
        }
        return 0;
    }
}