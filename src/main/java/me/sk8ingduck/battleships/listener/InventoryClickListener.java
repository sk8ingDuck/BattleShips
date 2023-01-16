package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        GameSession game = BattleShips.getGame();
        if (!game.isIngame()
                || event.getWhoClicked().getInventory().getItemInMainHand().equals(BattleShips.getSettingsConfig().getTntGunItem())) {
            event.setCancelled(true);
        }

        if (event.getWhoClicked() instanceof Player player && game.getTeam(player) == null) {
            event.setCancelled(true);
        }
    }
}
