package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class ArmorGui extends Gui {

    public ArmorGui() {
        gui = ChestMenu.builder(3).title("Rüstung").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot leather = gui.getSlot(10);
        Slot chain = gui.getSlot(12);
        Slot iron = gui.getSlot(14);
        Slot diamond = gui.getSlot(16);

        leather.setItem(new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName("Komplette Lederrüstung").setLores("", "§6Kosten§7: §a10 Emeralds").build());
        chain.setItem(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setDisplayName("Komplette Kettenrüstung").setLores("", "§6Kosten§7: §a20 Emeralds").build());
        iron.setItem(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName("Komplette Eisenrüstung").setLores("", "§6Kosten§7: §a30 Emeralds").build());
        diamond.setItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("Komplette Diamantrüstung").setLores("", "§6Kosten§7: §a40 Emeralds").build());

        leather.setClickHandler((player, clickInformation) -> buy(player, 10, 1));
        chain.setClickHandler((player, clickInformation) -> buy(player, 20, 2));
        iron.setClickHandler((player, clickInformation) -> buy(player, 30, 3));
        diamond.setClickHandler((player, clickInformation) -> buy(player, 40, 4));

    }

    private void buy(Player player, int cost, int type) {
        if (!player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), cost)) {
            player.sendMessage("§cDu hast nicht genug Emeralds.");
            return;
        }
        player.getInventory().removeItem(new ItemStack(Material.EMERALD, cost));

        if (type == 1) {
            player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        } else if (type == 2) {
            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        } else if (type == 3) {
            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        } else if (type == 4) {
            player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        }

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7F, 1.0F);
        player.closeInventory();
    }


}
