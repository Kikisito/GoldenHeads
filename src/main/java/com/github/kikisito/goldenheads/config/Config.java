package com.github.kikisito.goldenheads.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Config {
    @Setting
    private Boolean debug = false;

    @Setting
    public GoldenHeads goldenHeads = new GoldenHeads();

    @Setting
    public Recipe recipe = new Recipe();

    @Setting
    public Messages messages = new Messages();

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
                CustomModelData value for the Golden Head. Defaults to 1.
                """)
        @Setting
        private Integer customModelData = 1;

        @Comment("""
                Click type to apply the effects of the Golden Head. Defaults to RIGHT_CLICK. (RIGHT_CLICK, LEFT_CLICK, ANY)
                """)
        @Setting
        private String clickType = "RIGHT_CLICK";

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
        private String skullTexture = "https://textures.minecraft.net/texture/cfd27d8b218b5aa972fda9054926d7b1b2c0329a456332148fcc3d6c6d34cf0f"; // PhantomTupac Skin in Minecraft Servers

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

        public Integer getCustomModelData() {
            return customModelData;
        }

        public String getClickType() {
            return clickType;
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

        public String getSkullTexture() {
            return skullTexture;
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

    @ConfigSerializable
    public static class Recipe {
        @Comment("""
                Shape of the recipe. Use a 3x3 grid format.
                Use spaces for empty slots and any character for filled slots.
                The characters used will be mapped to the ingredients defined below.
                """)
        @Setting
        private List<String> shape = List.of(
                "GGG",
                "GPG",
                "GGG"
        );

        @Comment("""
                Ingredients used in the recipe.
                The keys should match the characters used in the shape.
                The values should be valid Material names.
                Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for valid names.
                """)
        @Setting
        private Map<Character, String> ingredients = Map.of(
                'G', "GOLD_INGOT",
                'P', "PLAYER_HEAD"
        );

        public List<String> getShape() {
            return shape;
        }

        public Map<Character, String> getIngredients() {
            return ingredients;
        }
    }

    @ConfigSerializable
    public static class Messages {
        @Setting
        private String noPermission = "&cYou do not have permission to execute this command.";

        @Setting
        private String playerNotFound = "&cPlayer not found: %s";

        @Setting
        private String invalidAmount = "&cInvalid amount. Please enter a valid number.";

        @Setting
        private String inventoryFull = "&eYour inventory is full. The Golden Head has been dropped on the ground.";

        @Setting
        private String gaveToAll = "&aGave %d golden head(s) to all online players.";

        @Setting
        private String gaveToPlayer = "&aGave %d golden head(s) to %s.";

        @Setting
        private String giveUsage = "&cUsage: /gheads give <*/player> [amount]";

        @Setting
        private String noArgs = "&6GoldenHeads &7v%s\n&7Author: &6Kikisito\n&7Commands:\n&6/gheads reload &7- Reload the configuration file.\n&6/gheads get &7- Get a golden head.\n&6/gheads give &7- Give a golden head to a player.";

        @Setting
        private String invalidPlayer = "&cInvalid player. Please enter a valid player.";

        @Setting
        private String onlyPlayer = "&cThis command can only be executed by a player.";

        @Setting
        private String gotGoldenHead = "&aYou have received a Golden Head!";

        @Setting
        private String reloadConfig = "&aGoldenHeads configuration reloaded successfully.";

        @Setting
        private String isOutdated = "&4&l[OUTDATED]";

        public String getNoPermission() {
            return noPermission;
        }

        public String getPlayerNotFound() {
            return playerNotFound;
        }

        public String getInvalidAmount() {
            return invalidAmount;
        }

        public String getInventoryFull() {
            return inventoryFull;
        }

        public String getGaveToAll() {
            return gaveToAll;
        }

        public String getGaveToPlayer() {
            return gaveToPlayer;
        }

        public String getGiveUsage() {
            return giveUsage;
        }

        public String getNoArgs() {
            return noArgs;
        }

        public String getInvalidPlayer() {
            return invalidPlayer;
        }

        public String getOnlyPlayer() {
            return onlyPlayer;
        }

        public String getGotGoldenHead() {
            return gotGoldenHead;
        }

        public String getReloadConfig() {
            return reloadConfig;
        }

        public String getIsOutdated() {
            return isOutdated;
        }
    }
}
