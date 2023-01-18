package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.util.LeaderboardSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.HashMap;

public class LeaderboardSignListener implements Listener {

	private HashMap<Player, LeaderboardSign> leaderboardSignHashMap = new HashMap<>();

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String line0 = event.getLine(0);
		if (line0 != null && line0.equalsIgnoreCase("stats")) {
			String line1 = event.getLine(1);
			if (line1 != null && line1.matches("^([1-9]\\d*)$")) {
				Player player = event.getPlayer();
				if (leaderboardSignHashMap.containsKey(player)) {
					player.sendMessage("§cDu musst erst den Spielerkopf setzen, bevor du neue Schilder erstellst.");
					return;
				}
				int rank = Integer.parseInt(line1);
				Location location = event.getBlock().getLocation();

				leaderboardSignHashMap.put(player, new LeaderboardSign(rank, location));
				Bukkit.broadcastMessage("§ePlatziere nun den Spielerkopf an die gewünsche Position.");
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlockPlaced().getType().equals(Material.PLAYER_HEAD)
		|| event.getBlockPlaced().getType().equals(Material.PLAYER_WALL_HEAD)) {
			Player player = event.getPlayer();
			LeaderboardSign leaderboardSign = leaderboardSignHashMap.get(player);
			if (leaderboardSign == null) return;

			leaderboardSign.setHeadLocation(event.getBlockPlaced().getLocation());
			leaderboardSignHashMap.remove(player);
			BattleShips.getSettingsConfig().addLeaderboardSign(leaderboardSign);
			Bukkit.broadcastMessage("§aLeaderboardschild gesetzt!");
		}
	}
}
