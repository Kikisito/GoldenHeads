package com.github.kikisito.goldenheads.config;

import com.google.inject.Inject;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationContainer<C> {
    private final AtomicReference<C> config;
    private final YamlConfigurationLoader loader;
    private final Class<C> clazz;
    private final String fileName;

    @Inject
    private ConfigurationContainer(
            C config,
            Class<C> clazz,
            YamlConfigurationLoader loader,
            String fileName
    ) {
        this.config = new AtomicReference<>(config);
        this.loader = loader;
        this.clazz = clazz;
        this.fileName = fileName;
    }

    public C get() {
        return this.config.get();
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try {
                final CommentedConfigurationNode node = loader.load();
                config.set(node.get(clazz));
            } catch (ConfigurateException exception) {
                throw new CompletionException("Could not load " + fileName + " file", exception);
            }
        });
    }

    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            try {
                final CommentedConfigurationNode node = loader.load();
                node.set(clazz, config.get());
                loader.save(node);
            } catch (ConfigurateException exception) {
                throw new CompletionException("Could not save " + fileName + " file", exception);
            }
        });
    }

    public static <C> ConfigurationContainer<C> load(Path path, Class<C> clazz) throws IOException {
        return load(path, clazz, "config.yml");
    }

    public static <C> ConfigurationContainer<C> load(Path path, Class<C> clazz, String fileName) throws IOException {
        path = path.resolve(fileName);
        final boolean firstCreation = Files.notExists(path);
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .header("Welcome to the GoldenHeads configuration file!")
                )
                .path(path)
                .build();

        final CommentedConfigurationNode node = loader.load();
        final C config = node.get(clazz);

        if (firstCreation) {
            node.set(clazz, config);
            loader.save(node);
        }

        ConfigurationContainer<C> container = new ConfigurationContainer<>(config, clazz, loader, fileName);
        container.save().join();

        return container;
    }
}