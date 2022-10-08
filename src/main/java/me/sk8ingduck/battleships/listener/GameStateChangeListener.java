package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        Bukkit.broadcastMessage("GameStateChange: " + event.getPreviousGameState() + " -> " + event.getNewGameState());

        if (event.getPreviousGameState() == GameState.LOBBY && event.getNewGameState() == GameState.WARMUP) {

        }

        if (event.getPreviousGameState() == GameState.WARMUP && event.getNewGameState() == GameState.INGAME) {

        }

        if (event.getPreviousGameState() == GameState.INGAME && event.getNewGameState() == GameState.RESTARTING) {

        }
    }
}
