package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

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

    public void setBannerLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.name() + ".bannerLocation", location);
        team.setBannerLocation(location);
        save();
    }

    public void setChestLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.name() + ".chestLocation", location);
        team.setChestLocation(location);
        save();
    }

    public void setTntGunLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.name() + ".tntGunLocation", location);
        team.setTntGunLocation(location);
        save();
    }
}