package me.sk8ingduck.battleships.game;

public enum GameState {

    LOBBY(30),
    WARMUP(30),
    INGAME(1230),
    RESTARTING(5);

    private final int seconds;
    GameState(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }
}
