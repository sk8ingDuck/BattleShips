package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		GameSession game = BattleShips.getGame();
		Player player = event.getPlayer();
        Block block = event.getBlock();

        if (event.getBlock().getType() == Material.COBWEB || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

		event.setCancelled(true);

		if (block.getType().equals(Material.EMERALD_BLOCK)) {
			event.setDropItems(false);

			game.getStats(player.getUniqueId()).addFarmedEmerald();
			player.getInventory().addItem(new ItemStack(Material.EMERALD));
		}

		if (block.getState() instanceof Banner) {
			for (Team team : game.getPlayingTeams()) {
				if (block.getLocation().getBlockX() == team.getBanner().getLocation().getBlockX()
						&& block.getLocation().getBlockY() == team.getBanner().getLocation().getBlockY()
						&& block.getLocation().getBlockZ() == team.getBanner().getLocation().getBlockZ()
						&& event.getBlock().getType().equals(team.getBanner().getItem())) {
					if (BattleShips.getGame().getTeam(player).equals(team)) {
						event.setCancelled(true);
					} else {
						event.setDropItems(false);
						event.setCancelled(!game.captureBanner(player, team));
					}
				}
			}
		}
		if (block.getState() instanceof Chest) {
			for (Team team : game.getPlayingTeams()) {
				if (block.getLocation().getBlockX() == team.getChest().getLocation().getBlockX()
						&& block.getLocation().getBlockY() == team.getChest().getLocation().getBlockY()
						&& block.getLocation().getBlockZ() == team.getChest().getLocation().getBlockZ()) {
					event.setCancelled(true);
				}
			}
		}
	}
}
