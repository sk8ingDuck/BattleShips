package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        Bukkit.broadcastMessage("GameStateChange: " + event.getPreviousGameState() + " -> " + event.getNewGameState());

        if (event.getPreviousGameState() == GameState.LOBBY && event.getNewGameState() == GameState.WARMUP) {
            BattleShips.getInstance().getGame().assignTeamToPlayers();
            for (Team team : Team.getActiveTeams())
                team.teleportPlayers();

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().clear();
                player.setFireTicks(0);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            });
        }

        if (event.getPreviousGameState() == GameState.WARMUP && event.getNewGameState() == GameState.INGAME) {

        }

        if (event.getPreviousGameState() == GameState.INGAME && event.getNewGameState() == GameState.RESTARTING) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.teleport(BattleShips.getInstance().getSettingsConfig().getLobbySpawn()));
        }
    }
}
