package me.sk8ingduck.battleships.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RestartCountdown extends Countdown {


    public RestartCountdown() {
        super();
    }

    @Override
    public void run() {
        int seconds = getSeconds().decrementAndGet();

        if (seconds == 0) {
            Bukkit.getServer().spigot().restart();
            return;
        }

        Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));
        Bukkit.broadcastMessage("§cDer Server wird in §6" + seconds + " Sekunde(n) §cneugestartet");
    }
}