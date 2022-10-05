package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Bukkit.getOnlinePlayers().size() >= 4) {
            BattleShips.getInstance().getGame().nextGameState();
        }
    }
}
