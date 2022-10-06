package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class Countdown implements Runnable {

    private boolean isRunning;

    private final AtomicInteger seconds;

    private BukkitTask bukkitTask;

    public Countdown(int seconds) {
        this.seconds = new AtomicInteger(seconds + 1);
    }

    public void run() {}


    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(BattleShips.getInstance(), this, 0, 20);
        isRunning = true;
    }

    public void stop() {
        bukkitTask.cancel();
        isRunning = false;
    }

    public AtomicInteger getSeconds() {
        return seconds;
    }
}