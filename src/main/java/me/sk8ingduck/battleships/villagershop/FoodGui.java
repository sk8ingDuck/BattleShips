package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class FoodGui extends Gui {

    public FoodGui() {
        gui = ChestMenu.builder(3).title("Essen").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot bread = gui.getSlot(10);
        Slot fish = gui.getSlot(12);
        Slot pork = gui.getSlot(14);
        Slot beef = gui.getSlot(16);

        bread.setItem(new ItemBuilder(Material.BREAD, 4).setLores("", "§6Kosten§7: §a2 Emeralds").build());
        fish.setItem(new ItemBuilder(Material.LEGACY_COOKED_FISH, 4).setLores("", "§6Kosten§7: §a3 Emeralds").build());
        pork.setItem(new ItemBuilder(Material.COOKED_PORKCHOP, 2).setLores("", "§6Kosten§7: §a4 Emeralds").build());
        beef.setItem(new ItemBuilder(Material.COOKED_BEEF).setLores("", "§6Kosten§7: §a5 Emeralds").build());

        bread.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.BREAD, 4), 2));
        fish.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.LEGACY_COOKED_FISH, 4), 3));
        pork.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.COOKED_PORKCHOP, 2), 4));
        beef.setClickHandler((player, clickInformation) -> buy(player, new ItemStack(Material.COOKED_BEEF, 1), 5));

    }


}
