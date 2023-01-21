package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

import java.awt.*;

public class ExtrasGui extends Gui {

    public ExtrasGui() {
        gui = ChestMenu.builder(3).title("Extras").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot speedPotion = gui.getSlot(10);
        Slot cobweb = gui.getSlot(12);
        Slot granade = gui.getSlot(14);
        Slot trap = gui.getSlot(16);
        speedPotion.setItem(new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60, 0)).setPotionColor(Color.AQUA).setDisplayName("Speed I").setLores("", "§6Kosten§7: §a10 Emeralds").build());
        cobweb.setItem(new ItemBuilder(Material.COBWEB, 2).setLores("", "§6Kosten§7: §a10 Emeralds").build());
        granade.setItem(new ItemBuilder(Material.SNOWBALL).setDisplayName("§cGranate").setLores("", "§6Kosten§7: §a20 Emeralds").build());
        trap.setItem(new ItemBuilder(Material.STRING).setDisplayName("§bFalle").setLores("", "§6Kosten§7: §a30 Emeralds").build());

        speedPotion.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60, 0)).setPotionColor(Color.AQUA).setDisplayName("Speed I").build(), 5));
        cobweb.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.COBWEB, 2).build(), 10));
        granade.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.SNOWBALL).setDisplayName("§cGranate").build(), 20));
        trap.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.STRING).build(), 30));
    }


}
