package com.github.kikisito.goldenheads.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class Config {
    @Setting
    private String noPermissionMessage = "<red>The specified server does not exist.</red>";

    @Setting
    private String serverNotFoundMessage = "<red>The specified server does not exist.</red>";

    @Setting
    private String usageMessage = "<yellow>Usage: /switch <server></yellow>";

    @Setting
    private List<String> blockedServers;
    @Comment("""
            
            Set to true if wonder to enable the permission maswitch.bypass (used to bypass the blockedServer restrictions.)
            """)
    private Boolean allowBypass = false;

    @Setting
    public AdminSettings admin = new AdminSettings();

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getServerNotFoundMessage() {
        return serverNotFoundMessage;
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public List<String> getBlockedServers() {
        return blockedServers;
    }

    public Boolean getAllowBypass() {
        return allowBypass;
    }

    @ConfigSerializable
    public static class AdminSettings {
        @Setting
        private String adminUsage = "<yellow>Usage: /rswitch <about/reload>";

        @Setting
        private String reloadMessage = "<green>Configuration reloaded successfully.</green>";

        @Setting
        private String aboutMessage = "<dark_aqua>RSwitch v%version% by Redactado & LutukiSolo</dark_aqua>";

        public String getAdminUsage() {
            return adminUsage;
        }

        public String getReloadMessage() {
            return reloadMessage;
        }

        public String getAboutMessage() {
            return aboutMessage;
        }
    }
}