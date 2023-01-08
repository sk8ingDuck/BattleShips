package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class MainMenuGui extends Gui {

    public MainMenuGui() {
        gui = ChestMenu.builder(3).title("Shop").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0000000b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot weapons = gui.getSlot(10);
        Slot food = gui.getSlot(11);
        Slot mining = gui.getSlot(12);
        Slot armor = gui.getSlot(13);
        Slot healing = gui.getSlot(14);
        Slot extras = gui.getSlot(15);
        Slot upgrades = gui.getSlot(16);

        weapons.setItem(new ItemBuilder(Material.IRON_SWORD).setDisplayName("Waffen").build());
        food.setItem(new ItemBuilder(Material.COOKED_PORKCHOP).setDisplayName( "Essen").build());
        mining.setItem(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName("Spitzhacken").build());
        armor.setItem(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setDisplayName("Rüstung").build());
        healing.setItem(new ItemBuilder(Material.POTION).setPotionColor(Color.RED).setDisplayName("Heilung").build());
        extras.setItem(new ItemBuilder(Material.COBWEB).setDisplayName("Extras").build());
        upgrades.setItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("Upgrades").build());

        weapons.setClickHandler((player, clickInformation) -> GuiManager.weaponsGui.open(player));
        food.setClickHandler((player, clickInformation) -> GuiManager.foodGui.open(player));
        mining.setClickHandler((player, clickInformation) -> GuiManager.miningGui.open(player));
        armor.setClickHandler((player, clickInformation) -> GuiManager.armorGui.open(player));
        healing.setClickHandler((player, clickInformation) -> GuiManager.healingGui.open(player));
        extras.setClickHandler((player, clickInformation) -> GuiManager.extrasGui.open(player));
        upgrades.setClickHandler((player, clickInformation) -> GuiManager.upgradesGui.open(player));
    }
}
