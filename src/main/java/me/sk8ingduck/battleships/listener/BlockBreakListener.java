package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GameSession game = BattleShips.getInstance().getGame();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                return;
            }
        }

        Block block = event.getBlock();
        if (block.getType().equals(Material.EMERALD_BLOCK)) {
            event.setDropItems(false);

            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD, 1));
        }

        if (block.getState() instanceof Banner) {
            if (game.getCurrentGameState() != GameState.INGAME) {
                event.setCancelled(true);
                return;
            }
            for (Team team : Team.getActiveTeams()) {
                if (block.getLocation().getBlockX() == team.getBannerLocation().getBlockX()
                && block.getLocation().getBlockY() == team.getBannerLocation().getBlockY()
                && block.getLocation().getBlockZ() == team.getBannerLocation().getBlockZ()
                && event.getBlock().getType().equals(team.getBanner().getType())) {

                    Player player = event.getPlayer();
                    Team playerTeam = game.getTeam(player);
                    if (playerTeam == null) {
                        event.setCancelled(true);
                        return;
                    }
                    if (playerTeam.equals(team)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (game.getBanner(player) != null) {
                        event.setCancelled(true);
                        player.sendMessage("§cDu hast bereits einen Banner. Bringe diesen erst in deine Base zurück.");
                        return;
                    }

                    event.setDropItems(false);
                    game.setBannerStolen(team, player);
                    team.removeCapturedBanner(team);
                    Bukkit.broadcastMessage("§eDer Banner von Team " + team + " §ewurde von " + playerTeam.getColor() + player.getName() + " §egenommen!");
                    player.getInventory().setHelmet(new ItemStack(team.getBanner()));
                }
            }
        }
    }
}
