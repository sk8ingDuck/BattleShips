package me.sk8ingduck.battleships.config;

import java.io.File;

public class SettingsConfig extends Config {

    private int lobbyCountdown;
    private int ingameCountdown;
    private int warmupCountdown;
    private int restartCountdown;

    public SettingsConfig(String name, File path) {
        super(name, path);

        if (getFileConfiguration().get("countdowns") == null) {
            getFileConfiguration().set("countdowns.lobby", 15);
            getFileConfiguration().set("countdowns.ingame", 720);
            getFileConfiguration().set("countdowns.warmup", 60);
            getFileConfiguration().set("countdowns.restart", 10);
        }

        this.lobbyCountdown = getFileConfiguration().getInt("countdowns.lobby");
        this.ingameCountdown = getFileConfiguration().getInt("countdowns.ingame");
        this.warmupCountdown = getFileConfiguration().getInt("countdowns.warmup");
        this.restartCountdown = getFileConfiguration().getInt("countdowns.restart");
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
    }

    public int getIngameCountdown() {
        return ingameCountdown;
    }

    public int getWarmupCountdown() {
        return warmupCountdown;
    }

    public int getRestartCountdown() {
        return restartCountdown;
    }
}
