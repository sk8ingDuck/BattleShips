package me.sk8ingduck.battleships.listener;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.config.TeamConfig;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		GameSession game = BattleShips.getGame();
		SettingsConfig config = BattleShips.getSettingsConfig();
		TeamConfig teamConfig = BattleShips.getTeamConfig();
		MessagesConfig msgsConfig = BattleShips.getMessagesConfig();

		Player player = event.getPlayer();

		if (game.getCurrentGameState() != null &&
				game.getCurrentGameState() != GameState.LOBBY) {
            event.setJoinMessage(null);
			game.setSpectator(player);
			return;
		}

		if (Bukkit.getOnlinePlayers().size() > teamConfig.getTeamSize() * teamConfig.getTeamCount()) {
			if (!event.getPlayer().hasPermission("chillsucht.join.server")) {
				event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX +
						"§cDie Runde ist voll. Hole dir §6Premium§c um vollen Runden beizutreten.");
				player.kickPlayer("");
				return;
			}

			List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

			Optional<Player> kickedPlayer = players.stream()
					.filter(otherPlayer -> !otherPlayer.hasPermission("chillsucht.join.server")).findAny();
			if (kickedPlayer.isPresent()) {
				kickedPlayer.get().sendMessage("§cDu wurdest von einem Spieler mit mehr Rechten gekickt. " +
						"Hole dir §6Premium§c um so etwas zu umgehen.");
				kickedPlayer.get().kickPlayer("");
			} else {
                player.sendMessage(ChillsuchtAPI.PREFIX + "§cDie Runde ist schon voll mit Premiums.");
                player.kickPlayer("");
            }

			return;
		}

		FastBoard fastBoard = new FastBoard(player);
		fastBoard.updateTitle(msgsConfig.get("sideboard.title"));
		int lineCount = 0;
		for (String line : msgsConfig.getSideboardLines()) {
			fastBoard.updateLine(lineCount++, line
					.replaceAll("&", "§")
					.replaceAll("%MAP%", config.getMapName())
					.replaceAll("%TEAM_COUNT%", String.valueOf(teamConfig.getTeamCount()))
					.replaceAll("%TEAM_SIZE%", String.valueOf(teamConfig.getTeamSize())));
		}

		game.setSideBoard(player, fastBoard);

		if (game.getStats(player.getUniqueId()) == null) {
			PlayerStats stats = BattleShips.getMySQL().getPlayerStats(player.getUniqueId());
			if (stats == null)
				stats = new PlayerStats(player.getUniqueId(), 0, 0, 0, 0, 0, 0, 0);

			game.setStats(player.getUniqueId(), stats);
		}

		player.setScoreboard(BattleShips.getScoreboard());

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
