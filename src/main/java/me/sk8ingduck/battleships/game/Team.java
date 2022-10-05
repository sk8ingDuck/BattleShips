package me.sk8ingduck.battleships.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Team {

    RED("Rot", ChatColor.RED, null),
    GREEN("Grün", ChatColor.GREEN, null),
    BLUE("Blau", ChatColor.BLUE, null),
    YELLOW("Gelb", ChatColor.YELLOW, null),
    WHITE("Weiss", ChatColor.WHITE, null),
    ORANGE("Orange", ChatColor.GOLD, null),
    PURPLE("Lila", ChatColor.DARK_PURPLE, null);

    private String name;
    private ChatColor color;
    private Location spawnLocation;
    private ArrayList<Player> members;
    Team(String name, ChatColor color, Location spawnLocation) {
        this.name = name;
        this.color = color;
        this.spawnLocation = spawnLocation;
        this.members = new ArrayList<>();
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
}
