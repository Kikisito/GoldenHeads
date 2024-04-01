package com.github.kikisito.goldenheads.commands;

import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.github.kikisito.goldenheads.managers.SubCommandDataManager;
import com.github.kikisito.goldenheads.managers.SubCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static com.github.kikisito.goldenheads.ColorTranslator.translate;

@SubCommandDataManager(
        name = "reload",
        description = "Reloads the configuration file.",
        permission = "goldenheads.admin"
)
public class Reload extends SubCommandManager {
    private final JavaPlugin plugin;
    private final ConfigMapper configMapper;
    private final BukkitAudiences audiences;

    @Inject
    public Reload(JavaPlugin plugin, ConfigMapper configMapper, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.configMapper = configMapper;
        this.audiences = audiences;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] strings) {
        Audience audience = audiences.sender(sender);

        Main main = (Main) plugin;
        main.reloadConfig();

        Component message = translate("&aConfiguration reloaded successfully!");
        audience.sendMessage(message);
    }
}
