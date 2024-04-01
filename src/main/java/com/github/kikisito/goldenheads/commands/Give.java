package com.github.kikisito.goldenheads.commands;

import com.github.kikisito.goldenheads.ColorTranslator;
import com.github.kikisito.goldenheads.GoldenHead;
import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.managers.SubCommandDataManager;
import com.github.kikisito.goldenheads.managers.SubCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.github.kikisito.goldenheads.ColorTranslator.translate;

@SubCommandDataManager(
        name = "give",
        description = "Give a golden head to a player.",
        permission = "goldenheads.admin"
)
public class Give extends SubCommandManager {
    private final JavaPlugin plugin;
    private final BukkitAudiences audiences;

    @Inject
    public Give(JavaPlugin plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        Audience audience = audiences.sender(sender);

        if (args.length < 1 || args.length > 2) {
            audience.sendMessage(translate("&cUsage: /gheads give <*/player> [amount]"));
            return;
        }

        String playerName = args[0];
        int amount = 1;

        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                audience.sendMessage(translate("&cInvalid amount. Please enter a valid number."));
                return;
            }
        }

        if (playerName.equals("*")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                giveGoldenHead(player, amount);
            }
            audience.sendMessage(translate("&aGave " + amount + " golden head(s) to all online players."));
        } else {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                audience.sendMessage(translate("&cPlayer not found: " + playerName));
                return;
            }
            giveGoldenHead(player, amount);
            audience.sendMessage(translate("&aGave " + amount + " golden head(s) to " + player.getName() + "."));
        }
    }

    private void giveGoldenHead(Player player, int amount) {
        Audience audience = audiences.player(player);
        Main main = (Main) plugin;
        ItemStack goldenHead = GoldenHead.createHead(main, Optional.empty());
        goldenHead.setAmount(amount);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), goldenHead);
            audience.sendMessage(ColorTranslator.translate("&eYour inventory is full. The Golden Head has been dropped on the ground."));
        } else {
            player.getInventory().addItem(goldenHead);
            audience.sendMessage(ColorTranslator.translate("&aYou have received a Golden Head!"));
        }
    }
}