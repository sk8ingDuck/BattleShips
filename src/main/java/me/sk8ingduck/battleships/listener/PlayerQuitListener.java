package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
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
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();

        if (game.getCurrentGameState() != null
                && game.getCurrentGameState() == GameState.LOBBY
                && Bukkit.getOnlinePlayers().size() <= config.getNeededPlayersToStart()) {
            GameState.LOBBY.getCountdown().resetCountdown();
            game.changeGameState(null);
        }

        Player player = event.getPlayer();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            event.setQuitMessage("§c" + player.getName() + " §ehat das Spiel verlassen.");
            Team playerTeam = game.getTeam(player);
            if (playerTeam != null) {
                playerTeam.removeMember(player);
            }
        }

        Team stolenBannerTeam = game.getBanner(player);
        if (stolenBannerTeam != null) {
            stolenBannerTeam.resetBanner();
            game.setBannerStolen(null, player);
            Bukkit.broadcastMessage("§eDer Banner von Team " + stolenBannerTeam + " §ewurde zurückgebracht!");
        }
    }
}
