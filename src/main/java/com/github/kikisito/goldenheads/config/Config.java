package com.github.kikisito.goldenheads.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class Config {
    @Setting
    private Boolean debug = false;

    @Setting
    public GoldenHeads goldenHeads = new GoldenHeads();

    @Comment("""
            Other settings related to Golden Heads
            """)
    @Setting
    private Boolean dropPlayerHeadOnDeath = true;

    @Setting
    private Boolean dropHeadOnlyWhenKilledByAPlayer = true;

    public Boolean getDebug() {
        return debug;
    }

    public Boolean getDropPlayerHeadOnDeath() {
        return dropPlayerHeadOnDeath;
    }

    public Boolean getDropHeadOnlyWhenKilledByAPlayer() {
        return dropHeadOnlyWhenKilledByAPlayer;
    }

    @ConfigSerializable
    public static class GoldenHeads {
        @Comment("""
                If you want to change the material, check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
                before modifying the value, otherwise it may break.
                It is also recommended to restart your server to register the new recipe correctly.
                """)
        @Setting
        private String material = "PLAYER_HEAD";

        @Comment("""
                Delay (in ticks) before applying the effects of the Golden Head. Set it to 0 to apply them instantly. (WORKS ONLY IN PAPER/SPIGOT)
                """)
        @Setting
        private Integer delay = 0;

        @Comment("""
                Amount of food the player will recover after eating the Golden Head. Each level of hunger has a value of 2. Defaults to 6.
                """)
        @Setting
        private Integer foodAmount = 6;

        @Comment("""
                Amount of saturation the player will recover after eating the Golden Head. Defaults to 14.4.
                """)
        @Setting
        private Double saturationAmount = 14.4;

        @Comment("""
                In case material is a player head.
                """)
        @Setting
        private String skullOwner = "PhantomTupac";

        @Comment("""
                Here you can use legacy format or MiniMessage format, both are supported.
                """)
        @Setting
        private String displayName = "&6Golden Head";

        @Setting
        private List<String> lore = List.of(
                "&eAbsorption II (2:00)",
                "&eRegeneration III (0:02)",
                "&eFire Resistance (0:10)"
        );

        @Comment("""
                Please, check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html if you
                are changing or adding potion effects. Any incorrect value can break the plugin.
                Format: EFFECT|DURATION (in seconds)|LEVEL
                """)
        @Setting
        private List<String> potionEffects = List.of(
                "ABSORPTION|120|2",
                "REGENERATION|2|3",
                "FIRE_RESISTANCE|10|1"
        );

        public String getMaterial() {
            return material;
        }

        public Integer getDelay() {
            return delay;
        }

        public Integer getFoodAmount() {
            return foodAmount;
        }

        public Double getSaturationAmount() {
            return saturationAmount;
        }

        public String getSkullOwner() {
            return skullOwner;
        }

        public String getDisplayName() {
            return displayName;
        }

        public List<String> getLore() {
            return lore;
        }

        public List<String> getPotionEffects() {
            return potionEffects;
        }
    }
}
