package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.type.ChestMenu;

public abstract class Gui {
    protected ChestMenu gui;

    public void open(Player player) {
        player.closeInventory();
        gui.open(player);
    }


    protected void buy(Player player, ItemStack item, int cost) {
        if (!player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), cost)) {
            player.sendMessage(BattleShips.getMessagesConfig().get("error.notEnoughEmeralds"));
            return;
        }
        player.getInventory().removeItem(new ItemStack(Material.EMERALD, cost));
        player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7F, 1.0F);
    }
}
