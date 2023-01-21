package me.sk8ingduck.battleships.command;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.mysql.PlayerStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GameSession game = BattleShips.getGame();
		MessagesConfig msgs = BattleShips.getMessagesConfig();

		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Not a player.");
				return true;
			}
			Player player = (Player) sender;
			PlayerStats stats = game.getStats(player.getUniqueId());
			player.sendMessage(stats.replace(BattleShips.getMessagesConfig().get("player.stats"), player.getName()));
			return true;
		}

		Pattern pattern = Pattern.compile("^#([1-9]\\d*)$"); //Regex for #<number>
		Matcher matcher = pattern.matcher(args[0]);
		if (matcher.matches()) {
			PlayerStats stats = BattleShips.getMySQL().getPlayerStats(Integer.parseInt(matcher.group(1)));
			if (stats == null) {
				sender.sendMessage(msgs.get("error.playerNotFound"));
				return true;
			}

			sender.sendMessage(stats.replace(BattleShips.getMessagesConfig().get("player.stats"),
					ChillsuchtAPI.getGeneralAPI().getNameFromUUID(stats.getUuid())));
			return true;
		}

		String playerName = args[0];
		UUID playerUUID = ChillsuchtAPI.getGeneralAPI().getUUIDFromName(playerName);
		PlayerStats stats = game.getStats(playerUUID);
		if (stats == null) {
			stats = BattleShips.getMySQL().getPlayerStats(playerUUID);
		}

		if (stats == null) {
			sender.sendMessage(msgs.get("error.playerNotFound"));
			return true;
		}

		sender.sendMessage(stats.replace(BattleShips.getMessagesConfig().get("player.stats"), playerName));
		return true;
	}

}
