package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
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

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        GameSession game = BattleShips.getGame();
        if (event.getPreviousGameState() == GameState.LOBBY && event.getNewGameState() == GameState.WARMUP) {
            game.assignTeamToPlayers();

            for (Team team : BattleShips.getTeamConfig().getActiveTeams()) {
                if (team.getSize() > 0) {
                    team.teleportPlayers();
                    team.getBanner().setBlock();
                    team.getChest().setBlock();
                    game.addPlayingTeam(team);
                } else {
                    team.getBanner().removeBlock();
                    team.getChest().removeBlock();
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
            Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.start"));
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setHealth(20);
                player.setFireTicks(0);
            });
        }

        if (event.getPreviousGameState() == GameState.INGAME && event.getNewGameState() == GameState.RESTARTING) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.teleport(BattleShips.getSettingsConfig().getLobbySpawn()));
        }
    }
}
