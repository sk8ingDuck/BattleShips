package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.villagershop.GuiManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof NPC villager) {
            if (villager.getName().equals("Shop")) {
                GuiManager.mainMenuGui.open(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }
}
