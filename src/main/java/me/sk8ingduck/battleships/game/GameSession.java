package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSession {

    private GameState currentGameState;

    //stores the teams which have at least 1 player at game start time
    private final ArrayList<Team> playingTeams;

    //stores the team of each player
    private final HashMap<Player, Team> playerTeam;

    //stores which player has currently a banner on his head
    private final HashMap<Player, Team> stolenBanners;


    public GameSession() {
        this.playingTeams = new ArrayList<>();
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
        if (currentGameState != null)
            currentGameState.stop();

        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(currentGameState, gameState));
        currentGameState = gameState;

        if (gameState != null)
            currentGameState.start();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void addPlayingTeam(Team team) {
        playingTeams.add(team);
    }

    public ArrayList<Team> getPlayingTeams() {
        return playingTeams;
    }

    public Team getTeam(Player player) {
        return playerTeam.get(player);
    }

    public void setTeam(Player player, Team team) {
        playerTeam.put(player, team);
        team.addMember(player);
    }

    public void removeTeam(Player player) {
        Team currentTeam = playerTeam.get(player);
        if (currentTeam == null) return;

        currentTeam.removeMember(player);
        playerTeam.remove(player);
    }

    public void assignTeamToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !playerTeam.containsKey(player))
                .forEach(player -> setTeam(player, getLeastPopulatedTeam()));
    }

    private Team getLeastPopulatedTeam() {
        Team leastPopulated = null;
        int leastPopulatedSize = 69;

        for (Team team : Team.getActiveTeams()) {
            if (team.getSize() == BattleShips.getSettingsConfig().getTeamSize())
                continue; //team is full

            if (team.getSize() != 0 && team.getSize() < leastPopulatedSize) { //team is not empty and has least members
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

    public Team getStolenBanner(Player player) {
        return stolenBanners.get(player);
    }

    public void removeStolenBanner(Player player) {
        stolenBanners.remove(player);
    }

    public boolean captureBanner(Player player, Team team) {
        Team teamOfPlayer = playerTeam.get(player);
        MessagesConfig msgs = BattleShips.getMessagesConfig();
        if (currentGameState != GameState.INGAME || teamOfPlayer == null || teamOfPlayer.equals(team)) {
            return false;
        }

        if (stolenBanners.get(player) != null) {
            player.sendMessage(msgs.get("error.alreadyHasBanner"));
            return false;
        }

        stolenBanners.put(player, team);
        team.removeCapturedBanner(team);
        player.getInventory().setHelmet(new ItemStack(team.getBanner()));
        Bukkit.broadcastMessage(msgs.get("game.bannerStolen")
                .replaceAll("%TEAM%", team.toString())
                .replaceAll("%PLAYER%", teamOfPlayer.getColor() + player.getName()));
        return true;
    }

}
