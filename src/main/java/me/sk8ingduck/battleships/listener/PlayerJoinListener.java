package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();
        if (game.getCurrentGameState() == null
                && Bukkit.getOnlinePlayers().size() >= config.getNeededPlayersToStart()) {
            game.changeGameState(GameState.LOBBY);
        }

        Player player = event.getPlayer();
        player.setScoreboard(BattleShips.getInstance().getScoreboard());

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.setExp(0);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            player.getInventory().setItem(4, config.getTeamChooserItem());

            if (config.getLobbySpawn() == null) Bukkit.broadcastMessage("NO LOBBY SPAWN SET!");
            else player.teleport(config.getLobbySpawn());

            event.setJoinMessage("§a" + player.getName() + " §ehat das Spiel betreten.");
        }

    }
}
