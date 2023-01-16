package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class BlockPlaceListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

        if (event.getBlockPlaced().getType().equals(Material.COBWEB)) {
	        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());

	        int entityId = new Random().nextInt(10000);
	        new BukkitRunnable() {
		        int state = 0;

		        @Override
		        public void run() {
			        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(entityId, blockPosition, state);
			        Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));

			        if (state == 10) {
				        block.setType(Material.AIR);
				        this.cancel();
			        }

			        state++;
		        }
	        }.runTaskTimer(BattleShips.getInstance(), 0, 20);

            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}

	}
}
