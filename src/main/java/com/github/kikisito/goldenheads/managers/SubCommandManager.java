package com.github.kikisito.goldenheads.managers;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SubCommandManager {
    public abstract void execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] strings);
}