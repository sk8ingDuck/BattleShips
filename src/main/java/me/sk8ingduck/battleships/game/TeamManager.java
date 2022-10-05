package me.sk8ingduck.battleships.game;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TeamManager {

    private HashMap<Player, Team> playerTeam;

    public TeamManager() {
        playerTeam = new HashMap<>();
    }

    public Team getTeam(Player player) {
        return playerTeam.get(player);
    }


}
