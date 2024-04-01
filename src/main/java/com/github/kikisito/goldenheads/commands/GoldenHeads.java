package com.github.kikisito.goldenheads.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.github.kikisito.goldenheads.managers.SubCommandDataManager;
import com.github.kikisito.goldenheads.managers.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GoldenHeads extends Command implements CommandExecutor {
    private final Injector injector;
    private final LinkedHashMap<String, SubCommandDataManager> subCommandDataMap = new LinkedHashMap<>();
    private final HashMap<SubCommandDataManager, SubCommandManager> subCommandMap = new HashMap<>();

    @Inject
    protected GoldenHeads(Injector injector, JavaPlugin plugin) {
        super("GoldenHeads");

        this.injector = injector;

        setAliases(List.of("gheads", "gh", "goldenheads"));
        setDescription("Main command of GoldenHeads command.");
        setPermission("goldenheads.admin");

        registerSubCommand(Reload.class);
        registerSubCommand(Get.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }

        SubCommandDataManager subCommandData = subCommandDataMap.getOrDefault(args[0], null);

        if (subCommandData == null) {
            return true;
        }

        SubCommandManager subCommand = subCommandMap.getOrDefault(subCommandData, null);

        if (subCommand == null) {
            return true;
        }

        if (!sender.hasPermission(subCommandData.permission())) {
            return true;
        }

        subCommand.execute(sender, args[0], Arrays.stream(args).skip(1).toArray(String[]::new));

        return true;
    }

    private void registerSubCommand(Class<? extends SubCommandManager> subCommandClass) {
        SubCommandManager subCommand = injector.getInstance(subCommandClass);

        SubCommandDataManager subCommandData = subCommand.getClass().getAnnotation(SubCommandDataManager.class);

        subCommandDataMap.put(subCommandData.name(), subCommandData);
        subCommandMap.put(subCommandData, subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return execute(sender, label, args);
    }
}
