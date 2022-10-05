package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Location;

import java.io.File;
import java.util.Objects;

public class TeamConfig extends Config {

    public TeamConfig(String name, File path) {
        super(name, path);


        if (getFileConfiguration().get("team") == null) {

            getFileConfiguration().set("team.RED.name", "Rot");
            getFileConfiguration().set("team.RED.color", "&c");
            getFileConfiguration().set("team.RED.spawnLocation", null);

            getFileConfiguration().set("team.GREEN.name", "Grün");
            getFileConfiguration().set("team.GREEN.color", "&a");
            getFileConfiguration().set("team.GREEN.spawnLocation", null);

            getFileConfiguration().set("team.BLUE.name", "Blau");
            getFileConfiguration().set("team.BLUE.color", "&9");
            getFileConfiguration().set("team.BLUE.spawnLocation", null);

            getFileConfiguration().set("team.YELLOW.name", "Gelb");
            getFileConfiguration().set("team.YELLOW.color", "&e");
            getFileConfiguration().set("team.YELLOW.spawnLocation", null);

            getFileConfiguration().set("team.WHITE.name", "Weiss");
            getFileConfiguration().set("team.WHITE.color", "&f");
            getFileConfiguration().set("team.WHITE.spawnLocation", null);

            getFileConfiguration().set("team.ORANGE.name", "Orange");
            getFileConfiguration().set("team.ORANGE.color", "&6");
            getFileConfiguration().set("team.ORANGE.spawnLocation", null);

            getFileConfiguration().set("team.PURPLE.name", "Lila");
            getFileConfiguration().set("team.PURPLE.color", "&d");
            getFileConfiguration().set("team.PURPLE.spawnLocation", null);

            getFileConfiguration().set("team.GRAY.name", "Lila");
            getFileConfiguration().set("team.GRAY.color", "&d");
            getFileConfiguration().set("team.GRAY.spawnLocation", null);

            save();
        }
    }

    public String getName(Team team) {
        return getFileConfiguration().getString("team." + team.name() + ".name");
    }

    public String getColor(Team team) {
        return Objects.requireNonNull(getFileConfiguration().getString("team." + team.name() + ".color"))
                .replaceAll("&", "§");
    }

    public Location getSpawnLocation(Team team) {
        return getFileConfiguration().getLocation("team." + team.name() + ".spawnLocation");
    }

    public void setSpawnLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.name() + ".spawnLocation", location);
        save();
    }

}