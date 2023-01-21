package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        GameSession game = BattleShips.getGame();
        if (!game.isIngame()) {
            player.teleport(BattleShips.getSettingsConfig().getLobbySpawn());
        } else {
            Team playerTeam = game.getTeam(player);
            Bukkit.getScheduler().scheduleSyncDelayedTask(BattleShips.getInstance(),
                    () -> playerTeam.teleportPlayer(player), 0);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

            player.getInventory().addItem(new ItemBuilder(Material.STONE_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).build());
        }
    }
}
