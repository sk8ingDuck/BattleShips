package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RestartCountdown extends Countdown {


    public RestartCountdown(int seconds) {
        super(seconds);
    }

    @Override
    public void run() {
        int counter = getCountdown().decrementAndGet();

        if (counter > 0) {
            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(counter));
            Bukkit.broadcastMessage("§cDer Server wird in §6" + counter + " Sekunde(n) &cneugestartet");
        } else {
            BattleShips.getInstance().getGame().nextGameState();
            stop();
        }
    }
}