package com.github.kikisito.goldenheads;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.github.kikisito.goldenheads.config.Config;
import com.github.kikisito.goldenheads.config.ConfigurationContainer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class  Logger {
    private final MiniMessage miniMessage;
    private final String prefix;
    private final Audience console;
    private static ConfigurationContainer<Config> config; //

    @Inject
    public Logger(JavaPlugin plugin) {
        this.miniMessage = MiniMessage.miniMessage();
        this.prefix = "<white>[<gold>GoldenHeads<white>] ";
        this.console = BukkitAudiences.create(plugin).console();
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void debug(String message) {
        if (config.get().getDebug()) {
            log(LogLevel.DEBUG, message);
        }
    }

    private void log(LogLevel level, String message) {
        String formattedMessage = prefix + level.getColor() + message;
        console.sendMessage(miniMessage.deserialize(formattedMessage));
    }

    private enum LogLevel {
        INFO("<#2e93ff>"),
        WARN("<#cccf1f>"),
        ERROR("<#fc283a>"),
        DEBUG("<#3afcf6>");

        private final String color;

        LogLevel(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
