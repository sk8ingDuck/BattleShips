package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.mysql.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameSession {

    private GameState currentGameState;

    //stores the teams which have at least 1 player at game start time
    private final ArrayList<Team> playingTeams;

    //stores the team of each player
    private final HashMap<Player, Team> playerTeam;

    //stores which player has currently a banner on his head
    private final HashMap<Player, Team> stolenBanners;

    //store the stats of each player
    private final HashMap<UUID, PlayerStats> playerStats;

    public GameSession() {
        this.playingTeams = new ArrayList<>();
        this.playerTeam = new HashMap<>();
        this.stolenBanners = new HashMap<>();
        this.playerStats = new HashMap<>();
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

        GameStateChangeEvent event = new GameStateChangeEvent(currentGameState, gameState);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        currentGameState = gameState;

        if (currentGameState != null)
            currentGameState.start();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public boolean isIngame() {
        return currentGameState == GameState.WARMUP || currentGameState == GameState.INGAME || currentGameState == GameState.RESTARTING;
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
        if (currentGameState != GameState.INGAME || teamOfPlayer == null) {
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

    public void checkWin(Team team) {
        if (team.getCapturedBanners() == playingTeams.size()) {
            Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.teamWin").replaceAll("%TEAM%", team.toString()));
            team.addWin();
            changeGameState(GameState.RESTARTING);
        }
    }


    public boolean checkWin() {
        Team winnerTeam = null;
        for (Team team : playingTeams) {
            if (team.getSize() > 0) {
                if (winnerTeam == null) {
                    winnerTeam = team;
                } else { //at least 2 teams left
                    return false;
                }
            }
        }
        if (winnerTeam == null) { //should not happen
            changeGameState(GameState.RESTARTING);
            return true;
        }
        //only one team left
        Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.teamWin").replaceAll("%TEAM%", winnerTeam.toString()));
        winnerTeam.addWin();
        changeGameState(GameState.RESTARTING);
        return true;
    }

    public PlayerStats getStats(UUID uuid) {
        return playerStats.get(uuid);
    }

    public void setStats(UUID uuid, PlayerStats stats) {
        playerStats.put(uuid, stats);
    }

    public void saveStats() {
        playerStats.forEach((player, stats) -> BattleShips.getMySQL().savePlayerStats(player, stats));
    }

    public void saveStats(UUID uuid) {
        BattleShips.getMySQL().savePlayerStats(uuid, playerStats.get(uuid));
        playerStats.remove(uuid); //remove player from playerStats cache otherwise dirty read could occur
    }

}
