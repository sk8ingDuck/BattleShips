package me.sk8ingduck.battleships.listener;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        GameSession game = BattleShips.getGame();
        MessagesConfig msgs = BattleShips.getMessagesConfig();

        if (game.getCurrentGameState() != null
                && game.getCurrentGameState() == GameState.LOBBY
                && Bukkit.getOnlinePlayers().size() <= BattleShips.getSettingsConfig().getNeededPlayersToStart()) {
            GameState.LOBBY.resetCountdown();
            game.changeGameState(null);
        }

        Player player = event.getPlayer();
        Team playerTeam = game.getTeam(player);

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            event.setQuitMessage(msgs.get("player.leaveMessage").replaceAll("%PLAYER%", player.getName()));
            if (playerTeam != null) {
                playerTeam.removeMember(player);
            }
            ChillsuchtAPI.getPermissionAPI().removeRank(player, BattleShips.getInstance().getScoreboard());
            return;
        }

        Team bannerOnHead = game.getStolenBanner(player);
        if (bannerOnHead != null) {
            bannerOnHead.resetBanner();
            game.removeStolenBanner(player);
            Bukkit.broadcastMessage(msgs.get("game.bannerReturned").replaceAll("%TEAM%", bannerOnHead.toString()));
        }
    }
}
