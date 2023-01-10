package me.sk8ingduck.battleships.listener;

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
        Player player = event.getPlayer();

        GameSession game = BattleShips.getGame();
        MessagesConfig msgs = BattleShips.getMessagesConfig();

        game.getSideBoard(player).delete();

        if (game.getCurrentGameState() != null
                && game.getCurrentGameState() == GameState.LOBBY
                && Bukkit.getOnlinePlayers().size() <= BattleShips.getSettingsConfig().getNeededPlayersToStart()) {
            GameState.LOBBY.resetCountdown();
            game.changeGameState(null);
        }

        game.saveStats(player.getUniqueId());

        Team playerTeam = game.getTeam(player);
        if (playerTeam != null) {
            playerTeam.removeMember(player);
        }

        event.setQuitMessage(msgs.get("player.leaveMessage").replaceAll("%PLAYER%", player.getName()));
        //ChillsuchtAPI.getPermissionAPI().removeRank(player, BattleShips.getScoreboard());

        if (game.checkWin()) {
            return;
        }

        Team bannerOnHead = game.getStolenBanner(player);
        if (bannerOnHead != null) {
            bannerOnHead.resetBanner();
            bannerOnHead.addCapturedBanner(bannerOnHead);
            game.checkWin(bannerOnHead);
            game.removeStolenBanner(player);
            Bukkit.broadcastMessage(msgs.get("game.bannerReturned").replaceAll("%TEAM%", bannerOnHead.toString()));
        }
    }
}
