package me.sk8ingduck.battleships.config;

import java.io.File;

public class SettingsConfig extends Config {

    private final int maxPlayers;
    private final int neededPlayersToStart;

    public SettingsConfig(String name, File path) {
        super(name, path);

        this.maxPlayers = (int) checkPathOrSet("maxPlayers", 12);
        this.neededPlayersToStart = (int) checkPathOrSet("neededPlayersToStart", 1);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNeededPlayersToStart() {
        return neededPlayersToStart;
    }
}
