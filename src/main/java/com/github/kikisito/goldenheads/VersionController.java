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
        this.version = plugin.getDescription().getVersion().replaceAll("^[^\\d]+", "");
        this.logger = logger;
        IS_OUTDATED = isOutdated();
    }

    public String getLatestTagFromGitHub() {
        try {
            URL url = new URL("https://api.github.com/repos/Kikisito/GoldenHeads/releases/latest");
            InputStream inputStream = url.openStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return response.split("\"tag_name\":\"")[1].split("\"")[0].replaceAll("^[^\\d]+", "");
        } catch (IOException e) {
            return version;
        }
    }

    public int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("[^\\da-zA-Z]+");
        String[] parts2 = version2.split("[^\\da-zA-Z]+");
        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            String p1 = i < parts1.length ? parts1[i] : "0";
            String p2 = i < parts2.length ? parts2[i] : "0";

            try {
                int v1 = Integer.parseInt(p1);
                int v2 = Integer.parseInt(p2);
                if (v1 != v2) return Integer.compare(v1, v2);
            } catch (NumberFormatException e) {
                boolean p1Snapshot = p1.equalsIgnoreCase("SNAPSHOT");
                boolean p2Snapshot = p2.equalsIgnoreCase("SNAPSHOT");
                
                if (p1Snapshot && !p2Snapshot) return -1;
                if (!p1Snapshot && p2Snapshot) return 1;
                if (p1Snapshot) continue;

                int cmp = p1.compareTo(p2);
                if (cmp != 0) return cmp;
            }
        }
        return 0;
    }

    public boolean isOutdated() {
        String latest = getLatestTagFromGitHub();
        return latest != null && compareVersions(version, latest) < 0;
    }
}
