package me.sk8ingduck.battleships.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Snowball) {
			Player player = (Player) event.getEntity().getShooter();
			if (event.getHitEntity() != null) {
				Location location = event.getHitEntity().getLocation();
				location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 5, false, false);
			} else if (event.getHitBlock() != null) {
				Location location = event.getHitBlock().getLocation();
				location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 5, false, false);
			}
		}
	}
}
