package com.github.kikisito.goldenheads;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Path;

public class GoldenHeadsModule extends AbstractModule {
    private final JavaPlugin plugin;
    private final String version;

    public GoldenHeadsModule(JavaPlugin plugin) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(Logger.class).asEagerSingleton();
        bind(String.class).annotatedWith(Named.class).toInstance("config.yml");

        bind(VersionController.class).asEagerSingleton();
    }

    @Provides
    private Config provideConfig(ConfigurationContainer<Config> configurationContainer) {
        return configurationContainer.get();
    }

    @Provides
    private ConfigurationContainer<Config> provideConfigurationContainer(
            @Named("config.yml") String fileName,
            ConfigMapper configMapper
    ) throws IOException {
        Path configPath = plugin.getDataFolder().toPath().resolve(fileName);
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                )
                .path(configPath)
                .build();

        ConfigurationContainer<Config> configContainer = ConfigurationContainer.load(plugin.getDataFolder().toPath(), Config.class, fileName);
        configMapper.register(Config.class, configContainer);

        return configContainer;
    }

    @Provides
    private ConfigMapper provideConfigMapper() {
        return new ConfigMapper();
    }

    @Provides
    private BukkitAudiences provideBukkitAudiences() {
        return BukkitAudiences.create(plugin);
    }
}
