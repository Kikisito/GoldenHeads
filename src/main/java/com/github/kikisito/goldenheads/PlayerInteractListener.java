package com.github.kikisito.goldenheads;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getItem() != null && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getItem().getType() == Material.PLAYER_HEAD && e.getItem().getItemMeta().hasEnchant(Enchantment.MENDING)){
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                Player player = e.getPlayer();
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 100, 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));
            }
        }
    }
}
