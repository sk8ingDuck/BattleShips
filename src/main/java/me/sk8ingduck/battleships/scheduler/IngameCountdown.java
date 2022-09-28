package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IngameCountdown extends Countdown {

    public IngameCountdown(int seconds) {
        super(seconds);
    }

    @Override
    public void run() {
        int counter = getCountdown().decrementAndGet();

        if (counter > 0) {
            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(counter));
        } else {
            BattleShips.getInstance().getGame().nextGameState();
            stop();
        }
    }
}
