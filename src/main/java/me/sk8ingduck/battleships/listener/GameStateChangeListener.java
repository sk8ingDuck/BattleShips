package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.event.GameStateChangeEvent;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        GameSession game = BattleShips.getGame();
        if (event.getPreviousGameState() == GameState.LOBBY && event.getNewGameState() == GameState.WARMUP) {
            game.assignTeamToPlayers();

            for (Team team : Team.getActiveTeams()) {
                if (team.getSize() > 0) {
                    team.teleportPlayers();
                    team.resetBanner();
                    game.addPlayingTeam(team);
                } else {
                    team.removeBannerAndChest();
                }
            }
            if (game.getPlayingTeams().size() < 2) {
                event.setCancelled(true);
                game.changeGameState(GameState.RESTARTING);
                Bukkit.broadcastMessage("§cNot enough teams.");
                return;
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().addItem(new ItemBuilder(Material.STONE_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).build());
                game.getStats(player.getUniqueId()).addGamePlayed();
            });

            game.updateBoards();
        }

        if (event.getPreviousGameState() == GameState.WARMUP && event.getNewGameState() == GameState.INGAME) {
        }

        if (event.getPreviousGameState() == GameState.INGAME && event.getNewGameState() == GameState.RESTARTING) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.teleport(BattleShips.getSettingsConfig().getLobbySpawn()));
        }
    }
}
