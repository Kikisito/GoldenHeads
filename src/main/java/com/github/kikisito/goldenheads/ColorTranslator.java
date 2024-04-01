package com.github.kikisito.goldenheads;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {
    private static final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static String toMM(String str) {
        str = str.replace("§", "&")
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<grey>")
                .replace("&8", "<dark_grey>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obf>")
                .replace("&l", "<b>")
                .replace("&m", "<st>")
                .replace("&n", "<u>")
                .replace("&o", "<i>")
                .replace("&r", "<reset>");

        if (str.contains("#")) {
            Matcher matcher = hexPattern.matcher(str);
            StringBuilder buffer = new StringBuilder();
            while (matcher.find()) {
                String replacement = String.format("<#%s>", matcher.group(1));
                matcher.appendReplacement(buffer, replacement);
            }
            matcher.appendTail(buffer);
            str = buffer.toString();
        }

        return str;
    }

    public static Component translate(String message) {
        return MiniMessage.miniMessage().deserialize(toMM(message));
    }
}