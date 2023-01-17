package me.sk8ingduck.battleships.listener;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Snowball) {
			Player player = (Player) event.getEntity().getShooter();
			Location location = null;
			if (event.getHitEntity() != null) {
				location = event.getHitEntity().getLocation();
			} else if (event.getHitBlock() != null) {
				location = event.getHitBlock().getLocation().add(0, 1.5, 0);
			}

			if (location != null) {
				TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);

				tnt.setSource(player);
				tnt.setTicksLived(1);
				tnt.setFuseTicks(1);
				tnt.setYield(2.5f);
			}
		}
	}
}
