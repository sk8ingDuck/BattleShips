package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();

        if (game.getCurrentGameState() == GameState.LOBBY
                && Bukkit.getOnlinePlayers().size() <= config.getNeededPlayersToStart()) {
            game.changeGameState(null);
        }
    }
}
