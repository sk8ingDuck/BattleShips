package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.scheduler.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GameSession {

    //stores the team of each player
    private final HashMap<Player, Team> playerTeam;

    //stores which player has currently a banner on his head
    private final HashMap<Player, Team> stolenBanners;

    private GameState currentGameState;

    private Countdown currentCountdown;

    public GameSession() {
        this.playerTeam = new HashMap<>();
        this.stolenBanners = new HashMap<>();
    }
    public void nextGameState() {
        int index = 0;
        for (int i = 0; i < GameState.values().length; i++) {
            if (GameState.values()[i] == currentGameState) {
                index = i;
            }
        }
        index = index >= GameState.values().length ? 0 : index + 1;

        changeGameState(GameState.values()[index]);
    }

    public void changeGameState(GameState gameState) {
        if (currentCountdown != null)
            currentCountdown.stop();

        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(currentGameState, gameState));
        currentGameState = gameState;

        if (gameState == null) return;

        currentCountdown = gameState.getCountdown();
        currentCountdown.start();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
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

    public void assignTeamToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !playerTeam.containsKey(player))
                .forEach(player -> setTeam(player, getLeastPopulatedTeam()));
    }
    private Team getLeastPopulatedTeam() {
        Team leastPopulated = null;
        int leastPopulatedSize = 69;

        for (Team team : Team.getActiveTeams()) {
            if (team.getSize() == BattleShips.getInstance().getSettingsConfig().getTeamSize())
                continue; //team is full

            if (team.getSize() != 0 && team.getSize() < leastPopulatedSize) {
                leastPopulatedSize = team.getSize();
                leastPopulated = team;
            }

        }

        if (leastPopulated != null) return leastPopulated;

        for (Team team : Team.getActiveTeams())
            if (team.getSize() == 0)
                return team;

        return null;
    }

    public Team getBanner(Player player) {
        return stolenBanners.get(player);
    }

    public void setBannerStolen(Team team, Player player) {
        stolenBanners.put(player, team);
    }
    public void removeStolenBanner(Player player) {
        stolenBanners.remove(player);
    }
}
