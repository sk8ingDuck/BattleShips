package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener implements Listener {

	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		GameSession game = BattleShips.getGame();
		if (!game.isIngame()) {
			event.setCancelled(true);
		}
		if (event.getEntity() instanceof Player player && game.getTeam(player) == null) {
			event.setCancelled(true);
		}
	}
}
