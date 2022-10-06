package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        Bukkit.broadcastMessage("GameStateChange: " + event.getPreviousGameState() + " -> " + event.getNewGameState());
    }
}
