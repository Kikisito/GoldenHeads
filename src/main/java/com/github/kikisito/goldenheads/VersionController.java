package com.github.kikisito.goldenheads;

import com.google.inject.Inject;
import com.github.kikisito.goldenheads.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class VersionController {
    public static boolean IS_OUTDATED;

    private String version;
    private Logger logger;

    @Inject
    public VersionController(JavaPlugin plugin, Logger logger) {
        this.version = plugin.getDescription().getVersion();
        IS_OUTDATED = isOutdated();

        logger.debug("VersionController has been initialized.");
        logger.debug("[VERCONTROLLER] Version: " + version);

        this.logger = logger;
    }

    public String getLatestTagFromGitHub() {
        try {
            URL url = new URL("https://api.github.com/repos/Kikisito/GoldenHeads/releases/latest");
            InputStream inputStream = url.openStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            String latestTag = response.split("\"tag_name\":\"")[1].split("\"")[0];

            return latestTag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (part1 < part2) {
                return -1;
            } else if (part1 > part2) {
                return 1;
            }
        }
        return 0;
    }

    public boolean isOutdated() {
        // Use CompareVersions method to check if the plugin is outdated, among with the latest version from GitHub
        return compareVersions(version, Objects.requireNonNull(getLatestTagFromGitHub())) < 0;
    }

    public String getVersion() {
        return version;
    }
}
