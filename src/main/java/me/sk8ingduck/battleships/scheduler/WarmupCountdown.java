package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WarmupCountdown extends Countdown {


    public WarmupCountdown() {
        super();
    }

    @Override
    public void run() {
        int seconds = getSeconds().decrementAndGet();

        if (seconds == 0) {
            BattleShips.getInstance().getGame().nextGameState();
            return;
        }

        Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(seconds));
        if (seconds % 5 == 0 || seconds <= 3) {
            Bukkit.broadcastMessage("§aDie Runde beginnt in §6" + seconds + " Sekunden");
        }
    }

}
