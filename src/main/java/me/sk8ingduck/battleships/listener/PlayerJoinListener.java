package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();
        if (game.getCurrentGameState() == null
                && Bukkit.getOnlinePlayers().size() >= config.getNeededPlayersToStart()) {
            game.changeGameState(GameState.LOBBY);
        }

        Player player = event.getPlayer();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {

            player.getInventory().setItem(4, config.getTeamChooserItem());
        }

    }
}
