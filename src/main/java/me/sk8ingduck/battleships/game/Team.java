package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.TeamConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Team {

    RED("Rot", "&c", null),
    GREEN("Grün", "&a", null),
    BLUE("Blau", "&9", null),
    YELLOW("Gelb", "&e", null),
    WHITE("Weiss", "&f", null),
    ORANGE("Orange", "&6", null),
    PURPLE("Lila", "&d", null),
    GRAY("Grau", "&8", null);

    private final String name;
    private final String color;
    private final Location spawnLocation;
    private ArrayList<Player> members;

    private final TeamConfig teamConfig = BattleShips.getInstance().getTeamConfig();

    Team(String defaultName, String defaultColor, Location defaultLocation) {
        this.name = (String) teamConfig.checkPathOrSet("team." + name() + ".name", defaultName);
        this.color = (String) teamConfig.checkPathOrSet("team." + name() + ".color", defaultColor);
        this.spawnLocation = (Location) teamConfig.checkPathOrSet("team." + name() + ".spawnLocation", defaultLocation);

        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        teamConfig.setSpawnLocation(this, location);
    }

    public boolean addMember(Player player) {
        if (members.contains(player)) {
            return false;
        }

        members.add(player);
        return true;
    }

    public boolean removeMember(Player player) {
        if (!members.contains(player)) {
            return false;
        }

        members.remove(player);
        return true;
    }

    public void sendMessage(String message) {
        members.forEach(member -> member.sendMessage(message));
    }

    @Override
    public String toString() {
        return color + name;
    }
}