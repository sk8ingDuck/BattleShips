package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		GameSession game = BattleShips.getGame();
		Player player = event.getPlayer();

		if (game.isIngame()) {
			Team team = game.getTeam(player);
			if (team == null) {
				event.setCancelled(true);
				Bukkit.getOnlinePlayers().stream().filter(otherPlayer -> game.getTeam(otherPlayer) == null)
						.forEach(otherPlayer -> otherPlayer.sendMessage("§7" + player.getDisplayName() + ": §r" + event.getMessage()));
			} else {

				event.setFormat("§7[" + team  + "§7] " + team.getColor() + player.getName() + "§7: §r" + event.getMessage());
			}
		} else {
			event.setFormat(player.getDisplayName() + " §7: §r" + event.getMessage());
		}
	}
}
