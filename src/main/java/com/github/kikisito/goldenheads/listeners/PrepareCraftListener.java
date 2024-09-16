package com.github.kikisito.goldenheads.listeners;

import com.github.kikisito.goldenheads.GoldenHead;
import com.github.kikisito.goldenheads.Logger;
import com.github.kikisito.goldenheads.Main;
import com.github.kikisito.goldenheads.config.ConfigMapper;
import com.google.inject.Inject;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class PrepareCraftListener implements Listener {

    private final Main plugin;
    private static ConfigMapper configMapper;
    private Logger logger;

    @Inject
    public PrepareCraftListener(Main plugin, ConfigMapper configMapper, Logger logger) {
        this.plugin = plugin;
        this.configMapper = configMapper;
        this.logger = logger;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        logger.debug("Detected prepare item craft event.");

        ItemStack result = event.getInventory().getResult();
        if (result != null && result.isSimilar(GoldenHead.createHead(plugin, Optional.empty()))) {
            for (ItemStack ingredient : event.getInventory().getMatrix()) {
                if (ingredient != null && ingredient.hasItemMeta()) {
                    ItemMeta meta = ingredient.getItemMeta();
                    if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "golden_head"), PersistentDataType.STRING)) {
                        event.getInventory().setResult(null);

                        logger.debug("Cancelled crafting of golden head.");
                        break;
                    }
                    logger.debug("Ingredient does not have golden head tag.");
                }
            }
        }
    }
}
