package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class MiningGui extends Gui {

    public MiningGui() {
        gui = ChestMenu.builder(3).title("Spitzhacken").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot stonePickaxe = gui.getSlot(10);
        Slot ironPickaxe = gui.getSlot(12);
        Slot goldPickaxe = gui.getSlot(14);
        Slot diamondPickaxe = gui.getSlot(16);

        stonePickaxe.setItem(new ItemBuilder(Material.STONE_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).setLores("", "§6Kosten§7: §a10 Emeralds").build());
        ironPickaxe.setItem(new ItemBuilder(Material.IRON_PICKAXE).setLores("", "§6Kosten§7: §a15 Emeralds").build());
        goldPickaxe.setItem(new ItemBuilder(Material.GOLDEN_PICKAXE).setLores("", "§6Kosten§7: §a20 Emeralds").build());
        diamondPickaxe.setItem(new ItemBuilder(Material.DIAMOND_PICKAXE).setLores("", "§6Kosten§7: §a25 Emeralds").build());

        stonePickaxe.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.STONE_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).build(), 10));
        ironPickaxe.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.IRON_PICKAXE, 1), 15));
        goldPickaxe.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.GOLDEN_PICKAXE, 1), 20));
        diamondPickaxe.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.DIAMOND_PICKAXE, 1), 25));

    }

}
