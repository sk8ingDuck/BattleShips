package me.sk8ingduck.battleships.event;

import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameStateChangeEvent extends Event {


    private final static HandlerList handlerList = new HandlerList();

    private final GameState previousGameState;
    private final GameState newGameState;

    public GameStateChangeEvent(GameState previousGameState, GameState newGameState) {
        this.previousGameState = previousGameState;
        this.newGameState = newGameState;
    }


    public GameState getPreviousGameState() {
        return previousGameState;
    }

    public GameState getNewGameState() {
        return newGameState;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
