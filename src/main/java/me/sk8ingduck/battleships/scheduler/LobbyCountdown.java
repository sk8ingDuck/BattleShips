package me.sk8ingduck.battleships.scheduler;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyCountdown extends Countdown {


    public LobbyCountdown(int seconds) {
        super(seconds);
    }

    @Override
    public void run() {
        int counter = getCountdown().decrementAndGet();

        if (counter > 0) {
            Bukkit.getOnlinePlayers().forEach((Player p) -> p.setLevel(counter));
            if (counter % 5 == 0 || counter <= 3) {
                Bukkit.broadcastMessage("§aDas Spiel startet in §6" + counter + " Sekunden");
            }
        } else {
            BattleShips.getInstance().getGame().nextGameState();
            stop();
        }
    }

}
