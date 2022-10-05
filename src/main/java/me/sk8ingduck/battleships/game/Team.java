package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.TeamConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Team {

    RED,
    GREEN,
    BLUE,
    YELLOW,
    WHITE,
    ORANGE,
    PURPLE,
    GRAY;

    private String name;
    private String color;
    private Location spawnLocation;
    private ArrayList<Player> members;

    private final TeamConfig teamConfig = BattleShips.getInstance().getTeamConfig();

    Team() {
        this.name = teamConfig.getName(this);
        this.color = teamConfig.getColor(this);
        this.spawnLocation = teamConfig.getSpawnLocation(this);

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