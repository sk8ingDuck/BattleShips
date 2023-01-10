package me.sk8ingduck.battleships.event;

import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event implements Cancellable {


    private final static HandlerList handlerList = new HandlerList();

    private boolean cancelled;
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
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
