package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.scheduler.Countdown;
import org.bukkit.Bukkit;

public class GameSession {

    private GameState currentGameState;

    private Countdown currentCountdown;

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
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(currentGameState, gameState));

        currentGameState = gameState;
    }
}
