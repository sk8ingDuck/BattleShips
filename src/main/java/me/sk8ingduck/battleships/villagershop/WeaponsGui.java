package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class WeaponsGui extends Gui {

    public WeaponsGui() {
        gui = ChestMenu.builder(3).title("Waffen").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot woodenSword = gui.getSlot(10);
        Slot stoneSword = gui.getSlot(12);
        Slot ironSword = gui.getSlot(14);
        Slot diamondSword = gui.getSlot(16);

        woodenSword.setItem(new ItemBuilder(Material.WOODEN_SWORD).setLores("", "§6Kosten§7: §a5 Emeralds").build());
        stoneSword.setItem(new ItemBuilder(Material.STONE_SWORD).setLores("", "§6Kosten§7: §a10 Emeralds").build());
        ironSword.setItem(new ItemBuilder(Material.IRON_SWORD).setLores("", "§6Kosten§7: §a20 Emeralds").build());
        diamondSword.setItem(new ItemBuilder(Material.DIAMOND_SWORD).setLores("", "§6Kosten§7: §a30 Emeralds").build());

        woodenSword.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.WOODEN_SWORD, 1), 5));
        stoneSword.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.STONE_SWORD, 1), 10));
        ironSword.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.IRON_SWORD, 1), 20));
        diamondSword.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.DIAMOND_SWORD, 1), 30));

    }

}
