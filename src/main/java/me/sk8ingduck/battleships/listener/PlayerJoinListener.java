package me.sk8ingduck.battleships.listener;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.mysql.PlayerStats;
import me.sk8ingduck.battleships.util.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        GameSession game = BattleShips.getGame();
        SettingsConfig config = BattleShips.getSettingsConfig();
        MessagesConfig msgsConfig = BattleShips.getMessagesConfig();

        Player player = event.getPlayer();

        if (BattleShips.getGame().getCurrentGameState() != null &&
                BattleShips.getGame().getCurrentGameState() != GameState.LOBBY) {
            player.sendMessage(ChillsuchtAPI.PREFIX + "§cDas Spiel hat bereits angefangen.");
            player.kickPlayer(null);
            return;
        }

        if (Bukkit.getOnlinePlayers().size() > config.getTeamSize() * config.getTeamCount()) {
            if (event.getPlayer().hasPermission("chillsucht.join.server")) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                Collections.shuffle(players);
                for (Player current : players) {
                    if (!current.hasPermission("chillsucht.join.server")) {
                        if (players.size() == Bukkit.getMaxPlayers()) {
                            current.sendMessage("§cDu wurdest von einem Spieler mit mehr Rechten gekickt. " +
                                    "Hole dir §6Premium§c um so etwas zu umgehen.");
                            current.kickPlayer("");
                        }
                        return;
                    }
                }
                event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX + "§cDie Runde ist schon voll mit Premiums.");
                player.kickPlayer("");
            } else {
                event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX +
                        "§cDie Runde ist voll. Hole dir §6Premium§c um vollen Runden beizutreten.");
                player.kickPlayer("");
            }
            return;
        }

        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle("§bChill§9Sucht §7| §9BattleShips");
        game.setSideBoard(player, fastBoard);

        if (game.getStats(player.getUniqueId()) == null) {
            PlayerStats stats = BattleShips.getMySQL().getPlayerStats(player.getUniqueId());
            if (stats == null) stats = new PlayerStats(0, 0, 0, 0, 0, 0);
            game.setStats(player.getUniqueId(), stats);
        }

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
