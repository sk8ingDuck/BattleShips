package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
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

        if (!game.isIngame()) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                return;
            }
        }

        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType().equals(Material.EMERALD_BLOCK)) {
            event.setCancelled(true);
            event.setDropItems(false);

            game.getStats(player.getUniqueId()).addFarmedEmerald();
            player.getInventory().addItem(new ItemStack(Material.EMERALD));
        }

        if (block.getState() instanceof Banner) {
            if (game.getCurrentGameState() != GameState.INGAME) {
                event.setCancelled(true);
                return;
            }
            for (Team team : game.getPlayingTeams()) {
                if (block.getLocation().getBlockX() == team.getBannerLocation().getBlockX()
                && block.getLocation().getBlockY() == team.getBannerLocation().getBlockY()
                && block.getLocation().getBlockZ() == team.getBannerLocation().getBlockZ()
                && event.getBlock().getType().equals(team.getBanner().getType())) {
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
                if (block.getLocation().getBlockX() == team.getChestLocation().getBlockX()
                        && block.getLocation().getBlockY() == team.getChestLocation().getBlockY()
                        && block.getLocation().getBlockZ() == team.getChestLocation().getBlockZ()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
