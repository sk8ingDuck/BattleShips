package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class HealingGui extends Gui {

    public HealingGui() {
        gui = ChestMenu.builder(3).title("Heilung").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot healing = gui.getSlot(10);
        Slot regeneration = gui.getSlot(14);

        Slot healingThrow = gui.getSlot(12);
        Slot regenerationThrow = gui.getSlot(16);

        healing.setItem(new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 0)).setDisplayName("Sofortige Heilung").setLores("", "§6Kosten§7: §a5 Emeralds").build());
        regeneration.setItem(new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*10, 0)).setDisplayName("Regeneration").setLores("", "§6Kosten§7: §a10 Emeralds").build());
        healingThrow.setItem(new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 0)).setDisplayName("Sofortige Heilung").setLores("", "§6Kosten§7: §a5 Emeralds").build());
        regenerationThrow.setItem(new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*10, 0)).setDisplayName("Regeneration").setLores("", "§6Kosten§7: §a10 Emeralds").build());

        healing.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 1)).setDisplayName("Sofortige Heilung").build(), 5));
        regeneration.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.POTION).setPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*10, 0)).setDisplayName("Regeneration").build(), 5));
        healingThrow.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 1)).setDisplayName("Sofortige Heilung").build(), 10));
        regenerationThrow.setClickHandler((player, clickInformation) -> buy(player, new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*10, 0)).setDisplayName("Regeneration").build(), 10));

    }

}
