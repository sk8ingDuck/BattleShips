package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
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
        GameSession game = BattleShips.getGame();
        SettingsConfig config = BattleShips.getSettingsConfig();
        MessagesConfig msgsConfig = BattleShips.getMessagesConfig();

        Player player = event.getPlayer();
        player.setScoreboard(BattleShips.getScoreboard());

        if (game.getCurrentGameState() != null && game.getCurrentGameState() != GameState.LOBBY) {
            //TODO: spectator join
            player.kickPlayer(msgsConfig.get("error.gameAlreadyStarted"));
            event.setJoinMessage(null);
            return;
        }

        if (game.getCurrentGameState() == null
                && Bukkit.getOnlinePlayers().size() >= config.getNeededPlayersToStart()) {
            game.changeGameState(GameState.LOBBY);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.getInventory().setItem(4, config.getTeamChooserItem());

        if (config.getLobbySpawn() != null)
            player.teleport(config.getLobbySpawn());

        event.setJoinMessage(msgsConfig.get("player.joinMessage").replaceAll("%PLAYER%", player.getName()));

    }
}
