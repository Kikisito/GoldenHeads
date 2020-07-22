package com.github.kikisito.goldenheads;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Main extends JavaPlugin {
    private NamespacedKey key;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.registerRecipe();
    }

    @Override
    public void onDisable() {
        this.getServer().removeRecipe(key);
    }

    public void registerRecipe(){
        ItemStack goldenhead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = goldenhead.getItemMeta();
        ((SkullMeta) itemMeta).setOwningPlayer(this.getServer().getOfflinePlayer(UUID.fromString("57a8704d-b3f4-4c8f-bea0-64675011fe7b")));
        itemMeta.setDisplayName(Utils.parseMessage("&eGolden Head"));
        itemMeta.addEnchant(Enchantment.MENDING, 1, false);
        goldenhead.setItemMeta(itemMeta);
        key = new NamespacedKey(this, "golden_head");
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, goldenhead);
        shapedRecipe.shape("GGG", "GPG", "GGG");
        shapedRecipe.setIngredient('G', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('P', Material.PLAYER_HEAD);
        this.getServer().addRecipe(shapedRecipe);
    }
}
