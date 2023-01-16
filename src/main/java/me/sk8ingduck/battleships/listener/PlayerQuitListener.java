package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GameSession game = BattleShips.getGame();
        MessagesConfig msgs = BattleShips.getMessagesConfig();

        game.removeTeam(player);
        game.removeSideBoard(player);
        game.saveStats(player.getUniqueId());

        event.setQuitMessage(msgs.get("player.leaveMessage").replaceAll("%PLAYER%", player.getName()));

        if (game.getCurrentGameState() != null
                && game.getCurrentGameState() == GameState.LOBBY
                && Bukkit.getOnlinePlayers().size() <= BattleShips.getSettingsConfig().getNeededPlayersToStart()) {
            GameState.LOBBY.resetCountdown();
            game.changeGameState(null);
        }

        //ChillsuchtAPI.getPermissionAPI().removeRank(player, BattleShips.getScoreboard());

        if (game.isIngame() && game.checkWin()) {
            return;
        }

        game.resetBanner(player);
    }
}
