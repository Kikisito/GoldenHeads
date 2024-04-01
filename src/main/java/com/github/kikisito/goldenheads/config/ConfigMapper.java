package com.github.kikisito.goldenheads.config;

import com.google.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Optional;

@Singleton
public class ConfigMapper {
    private final LinkedHashMap<Class<?>, ConfigurationContainer<?>> configs = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <C> Optional<ConfigurationContainer<C>> get(Class<C> clazz) {
        return Optional.ofNullable((ConfigurationContainer<C>) configs.get(clazz));
    }

    public <C> void register(Class<C> clazz, ConfigurationContainer<C> config) {
        configs.put(clazz, config);
    }

    public void reload() {
        configs.values().forEach(ConfigurationContainer::reload);
    }
}