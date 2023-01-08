package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        GameSession game = BattleShips.getInstance().getGame();
        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY || game.getCurrentGameState() == GameState.RESTARTING) {
            player.teleport(BattleShips.getInstance().getSettingsConfig().getLobbySpawn());
        } else if (game.getCurrentGameState() == GameState.WARMUP || game.getCurrentGameState() == GameState.INGAME) {
            Team playerTeam = game.getTeam(player);
            Bukkit.getScheduler().scheduleSyncDelayedTask(BattleShips.getInstance(), () -> {
                playerTeam.teleportPlayer(player); //teleport to spawn location of team
            }, 1);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        }
    }
}
