package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public enum GameState {

    LOBBY( 15) {
        @Override
        public void execute() {
            int seconds = getSeconds().decrementAndGet();

            if (seconds == 0) {
                BattleShips.getGame().nextGameState();
                return;
            }

            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));
            if (seconds % 5 == 0 || seconds <= 3) {
                Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("countdown.lobby").replaceAll("%SECONDS%", String.valueOf(seconds)));
            }
        }
    },
    WARMUP(30) {
        @Override
        public void execute() {
            int seconds = getSeconds().decrementAndGet();

            if (seconds == 0) {
                BattleShips.getGame().nextGameState();
                return;
            }

            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));
            if (seconds % 5 == 0 || seconds <= 3) {
                Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("countdown.warmup").replaceAll("%SECONDS%", String.valueOf(seconds)));
            }
        }
    },
    INGAME(20) {
        @Override
        public void execute() {
            int seconds = getSeconds().decrementAndGet();

            if (seconds == 0) {
                Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.nobodyWon"));
                BattleShips.getGame().nextGameState();
                return;
            }

            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));
        }
    },
    RESTARTING(10) {
        @Override
        public void execute() {
            int seconds = getSeconds().decrementAndGet();

            if (seconds == 0) {
                Bukkit.getServer().spigot().restart();
                return;
            }

            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));

            if (seconds < 10)
                Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("countdown.restarting").replaceAll("%SECONDS%", String.valueOf(seconds)));
        }
    };


    private int seconds;
    private AtomicInteger currentSeconds;

    private BukkitTask bukkitTask;

    GameState(int defaultSeconds) {
        int seconds = (int) BattleShips.getSettingsConfig().getPathOrSet("countdown." + name(), defaultSeconds);
        setSeconds(seconds);
    }


    public abstract void execute();

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(BattleShips.getInstance(), this::execute, 0, 20);
    }

    public void stop() {
        bukkitTask.cancel();
    }

    public void resetCountdown() {
        this.currentSeconds = new AtomicInteger(seconds + 1);
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
        this.currentSeconds = new AtomicInteger(seconds + 1);
    }
    public AtomicInteger getSeconds() {
        return currentSeconds;
    }
}
