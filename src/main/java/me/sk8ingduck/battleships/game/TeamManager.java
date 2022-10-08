package me.sk8ingduck.battleships.game;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TeamManager {

    private final HashMap<Player, Team> playerTeam;

    public TeamManager() {
        playerTeam = new HashMap<>();
    }

    public void setTeam(Player player, Team team) {
        playerTeam.put(player, team);
        team.addMember(player);
    }

    public void removeTeam(Player player) {
        Team currentTeam = playerTeam.get(player);
        if (currentTeam == null) return;

        currentTeam.removeMember(player);
        playerTeam.put(player, null);
    }
    public Team getTeam(Player player) {
        return playerTeam.get(player);
    }
}
