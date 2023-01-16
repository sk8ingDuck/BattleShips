package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class TNTGunListener implements Listener {

	private final HashMap<UUID, ItemStack> bowCache = new HashMap<>();


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		GameSession game = BattleShips.getGame();

		ItemStack tntGunItem = BattleShips.getSettingsConfig().getTntGunItem();
		if (game.getTeam(player) == null) {
			return;
		}

		Team team = game.getTeam(player);
		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().equals(team.getTntGunLocation())) { //player is standing on tnt gun of his team
			if (!team.isCooldownActive()) {
				if (!bowCache.containsKey(player.getUniqueId())) {
					bowCache.put(player.getUniqueId(), player.getInventory().getItemInMainHand());
					player.getInventory().setItemInMainHand(tntGunItem);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 5));
				}
			}
		} else {
			if (bowCache.containsKey(player.getUniqueId())) {
				player.getInventory().setItemInMainHand(bowCache.get(player.getUniqueId()));
				bowCache.remove(player.getUniqueId());
				player.removePotionEffect(PotionEffectType.SLOW);
			}
		}
	}

	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (event.getBow().getType() == Material.CROSSBOW) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				Team team = BattleShips.getGame().getTeam(player);
				if (team == null) return;

				TNTPrimed tnt = player.getWorld().spawn(event.getProjectile().getLocation(), TNTPrimed.class);
				tnt.setVelocity(event.getProjectile().getVelocity());
				tnt.setIsIncendiary(false);
				tnt.setTicksLived(5);
				tnt.setFuseTicks(100);
				tnt.setCustomName(String.valueOf(team.getTntGunLevel() * 2));

				event.setCancelled(true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(BattleShips.getInstance(), () -> {
					team.setTntGunCooldown(5);
					if (bowCache.containsKey(player.getUniqueId())) {
						player.getInventory().setItemInMainHand(bowCache.get(player.getUniqueId()));
						bowCache.remove(player.getUniqueId());
						player.removePotionEffect(PotionEffectType.SLOW);
					}
				}, 5L);
			}
		}
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().equals(BattleShips.getSettingsConfig().getTntGunItem())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().equals(BattleShips.getSettingsConfig().getTntGunItem())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		if (event.getEntity() instanceof TNTPrimed tnt) {
			event.setCancelled(true);
			Location location = event.getEntity().getLocation();
			if (tnt.getCustomName() != null) {
				int power = Integer.parseInt(tnt.getCustomName());
				location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, false, false);
			}

		}
	}
}
