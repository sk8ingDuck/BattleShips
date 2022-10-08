package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Location;

import java.io.File;

public class TeamConfig extends Config {

    public TeamConfig(String name, File path) {
        super(name, path);
    }

    public void setSpawnLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.name() + ".spawnLocation", location);
        team.setSpawnLocation(location);
        save();
    }
}