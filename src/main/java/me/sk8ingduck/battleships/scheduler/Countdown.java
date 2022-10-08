package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class Countdown implements Runnable {

    private boolean isRunning;

    private int seconds;
    private AtomicInteger currentSeconds;

    private BukkitTask bukkitTask;

    public void run() {}


    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(BattleShips.getInstance(), this, 0, 20);
        isRunning = true;
    }

    public void stop() {
        bukkitTask.cancel();
        isRunning = false;

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