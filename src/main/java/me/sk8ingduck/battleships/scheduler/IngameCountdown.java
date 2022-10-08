package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IngameCountdown extends Countdown {

    public IngameCountdown() {
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
    }
}
