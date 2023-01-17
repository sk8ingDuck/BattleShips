package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		GameSession game = BattleShips.getGame();
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (!game.isIngame()
					|| BattleShips.getGame().getCurrentGameState() == GameState.WARMUP
					|| game.getTeam(player) == null) {
				event.setCancelled(true);
			}
		}

		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {

			event.setDamage(event.getDamage() / 3);
		}
	}
}
