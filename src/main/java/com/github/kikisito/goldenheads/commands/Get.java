package com.github.kikisito.goldenheads.commands;

import com.github.kikisito.goldenheads.ColorTranslator;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.google.inject.Inject;
import com.github.kikisito.goldenheads.GoldenHead;
import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.managers.SubCommandManager;
import com.github.kikisito.goldenheads.managers.SubCommandDataManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.github.kikisito.goldenheads.ColorTranslator.translate;

@SubCommandDataManager(
        name = "get",
        description = "Get a golden head.",
        permission = "goldenheads.admin"
)
public class Get extends SubCommandManager {
    private final JavaPlugin plugin;
    private final ConfigMapper configMapper;
    private final BukkitAudiences audiences;

    @Inject
    public Get(JavaPlugin plugin, ConfigMapper configMapper, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.configMapper = configMapper;
        this.audiences = audiences;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {


        if (!(sender instanceof Player player)) {
            Audience audience = audiences.sender(sender);
            audience.sendMessage(translate("&cThis command can only be executed by a player."));
            return;
        }

        Audience audience = audiences.sender(sender);
        ItemStack goldenHead = GoldenHead.createHead((Main) plugin, Optional.empty());
        int amount = 1;

        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                audience.sendMessage(translate("&cInvalid amount. Please enter a valid number."));
                return;
            }
        }

        giveGoldenHead(player, amount);
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



