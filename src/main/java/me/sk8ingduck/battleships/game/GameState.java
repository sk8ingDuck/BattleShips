package me.sk8ingduck.battleships.game;


import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.scheduler.*;

public enum GameState {

    LOBBY(new LobbyCountdown(), 15),
    WARMUP(new WarmupCountdown(), 30),
    INGAME(new IngameCountdown(), 20),
    RESTARTING(new RestartCountdown(), 10);

    private final Countdown countdown;
    GameState(Countdown countdown, int defaultSeconds) {
        this.countdown = countdown;
        int seconds = (int) BattleShips.getInstance()
                .getSettingsConfig().getPathOrSet("countdown." + name(), defaultSeconds);
        countdown.setSeconds(seconds);
    }

    public Countdown getCountdown() {
        return countdown;
    }
}
